package mutation.tool.util

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import mutation.tool.annotation.builder.JavaAnnotationBuilder
import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.context.InsertionPoint
import mutation.tool.context.adapter.AnnotationAdapter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

private const val FILE1 = "./src/test/resources/fakeProject/src/main/java/TarefasController.java"

internal class InspectorTest {

    @Test
    fun testGetAnnotations() {
        val annotations = mutableListOf<AnnotationAdapter>()

        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            annotations.addAll(context.annotations)
        }

        assertTrue(annotations.isNotEmpty())
        assertEquals(11, annotations.size)
    }

    @Test
    fun testIsSameClass() {
        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            if (context.getInsertionPoint() != InsertionPoint.CLASS || context.name != "TarefasController")
                continue

            val visitor = object:VoidVisitorAdapter<Any>() {
                override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
                    super.visit(n, arg)
                    if (n!!.nameAsString == "TarefasController") assertTrue(isSameClass(context, n))
                }
            }
            visitor.visit(JavaParser.parse(File(FILE1)), null)
        }
    }

    @Test
    fun testIsSameMethod() {
        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            if (context.getInsertionPoint() != InsertionPoint.METHOD) continue
            val string = context.toString()

            val visitor = object:VoidVisitorAdapter<Any>() {
                override fun visit(n: MethodDeclaration?, arg: Any?) {
                    super.visit(n, arg)
                    if (n!!.toString() == string) assertTrue(isSameMethod(context, n))
                }
            }
            visitor.visit(JavaParser.parse(File(FILE1)), null)
        }
    }

    @Test
    fun testIsSameProp() {
        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            if (context.getInsertionPoint() != InsertionPoint.PROPERTY) continue
            val string = context.toString()

            val visitor = object:VoidVisitorAdapter<Any>() {
                override fun visit(n: FieldDeclaration?, arg: Any?) {
                    super.visit(n, arg)
                    if (n!!.toString() == string) assertTrue(isSameProp(context, n))
                }
            }
            visitor.visit(JavaParser.parse(File(FILE1)), null)
        }
    }

    @Test
    fun testIsSameParameter() {
        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            if (context.getInsertionPoint() != InsertionPoint.PARAMETER) continue
            val string = context.toString()
            val line = context.beginLine
            val column = context.beginColumn

            val visitor = object:VoidVisitorAdapter<Any>() {
                override fun visit(n: Parameter?, arg: Any?) {
                    super.visit(n, arg)
                    if (n!!.toString() == string && n.range.get().begin.line == line &&
                            n.range.get().begin.column == column) assertTrue(isSameParameter(context, n))
                }
            }
            visitor.visit(JavaParser.parse(File(FILE1)), null)
        }
    }

    @Test
    fun testNumOfAnnotationsAttributes() {
        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            for (annotation in context.annotations) {
                val builder = JavaAnnotationBuilder(annotation.string)
                builder.build()
                if (!annotation.string.contains("("))
                    assertEquals(0, numOfAnnotationAttributes(builder.annotationExpr!!))
                else
                    assertEquals(annotation.string.split(",").size,
                            numOfAnnotationAttributes(builder.annotationExpr!!))
            }
        }
    }
}
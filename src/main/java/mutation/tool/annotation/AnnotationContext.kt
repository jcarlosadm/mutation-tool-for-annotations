package mutation.tool.annotation

import com.github.javaparser.JavaParser
import com.github.javaparser.Range
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.util.ClassVisitor
import mutation.tool.annotation.context.Context
import java.io.File
import java.util.*

fun getListOfAnnotationContext(javaFile: File):List<Context> {
    val compilationUnit = JavaParser.parse(javaFile)
    val visitor = ClassVisitor()
    visitor.visit(compilationUnit, null)

    return visitor.contexts
}

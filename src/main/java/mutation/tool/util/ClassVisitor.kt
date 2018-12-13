package mutation.tool.util

import com.github.javaparser.Range
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import mutation.tool.annotation.AnnotationContext
import java.util.*

class ClassVisitor:VoidVisitorAdapter<Any>() {

    val contexts = mutableListOf<AnnotationContext>()

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)

        extractClassContexts(n)

        for (field in n?.fields!!) {
            extractFieldContexts(field)
        }

        for (method in n.methods!!) {
            extractMethodContexts(method)
        }
    }

    private fun extractMethodContexts(method: MethodDeclaration) {
        val list = method.annotations
        extract(list, InsertionPoint.METHOD, method.range)
    }

    private fun extractFieldContexts(field: FieldDeclaration) {
        val list = field.annotations
        extract(list, InsertionPoint.PROPERTY, field.range)
    }

    private fun extractClassContexts(n: ClassOrInterfaceDeclaration?) {
        val list = n?.annotations
        extract(list, InsertionPoint.CLASS, n?.range)
    }

    private fun extract(list: NodeList<AnnotationExpr>?, insertionPoint: InsertionPoint, parentRange:Optional<Range>?) {
        if (list!!.isEmpty()) {
            contexts.add(AnnotationContext(null, insertionPoint).setRange(parentRange!!))
        } else {
            for (annotation in list) {
                contexts.add(AnnotationContext(annotation, insertionPoint).setRange(annotation.range))
            }
        }
    }
}
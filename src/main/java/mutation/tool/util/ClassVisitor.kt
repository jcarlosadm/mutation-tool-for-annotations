package mutation.tool.util

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import mutation.tool.annotation.context.ClassContext
import mutation.tool.annotation.context.Context
import mutation.tool.annotation.context.MethodContext
import mutation.tool.annotation.context.PropertyContext

class ClassVisitor:VoidVisitorAdapter<Any>() {

    val contexts = mutableListOf<Context>()

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)

        contexts.add(ClassContext(n!!))

        for (field in n?.fields!!) {
            contexts.add(PropertyContext(field))
        }

        for (method in n.methods!!) {
            contexts.add(MethodContext(method))
        }
    }
}
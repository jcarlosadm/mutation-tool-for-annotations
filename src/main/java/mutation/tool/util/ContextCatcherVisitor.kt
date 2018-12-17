package mutation.tool.util

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import mutation.tool.annotation.context.ClassContext
import mutation.tool.annotation.context.Context
import mutation.tool.annotation.context.MethodContext
import mutation.tool.annotation.context.PropertyContext

class ContextCatcherVisitor:VoidVisitorAdapter<Any>() {

    val contexts = mutableListOf<Context>()

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)
        contexts.add(ClassContext(n!!))
    }

    override fun visit(n: FieldDeclaration?, arg: Any?) {
        super.visit(n, arg)
        contexts.add(PropertyContext(n!!))
    }

    override fun visit(n: MethodDeclaration?, arg: Any?) {
        super.visit(n, arg)
        contexts.add(MethodContext(n!!))
    }
}
package mutation.tool.annotation.context

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

class ContextCatcherVisitor:VoidVisitorAdapter<Any>() {

    val contexts = mutableListOf<Context>()

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (n != null) contexts.add(ClassContext(n))
    }

    override fun visit(n: FieldDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (n != null) contexts.add(PropertyContext(n))
    }

    override fun visit(n: MethodDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (n != null) contexts.add(MethodContext(n))
    }
}
package mutation.tool.context

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import mutation.tool.context.entity.Entity

class ContextCatcherVisitor:VoidVisitorAdapter<Any>() {

    val contexts = mutableListOf<Context>()

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (n != null) contexts.add(ClassContext(Entity(n)))
    }

    override fun visit(n: FieldDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (n != null) contexts.add(PropertyContext(Entity(n)))
    }

    override fun visit(n: MethodDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (n != null) contexts.add(MethodContext(Entity(n)))
    }

    override fun visit(n: Parameter?, arg: Any?) {
        super.visit(n, arg)
        if (n != null) contexts.add(ParameterContext(Entity(n)))
    }
}
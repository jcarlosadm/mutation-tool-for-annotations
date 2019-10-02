package mutation.tool.context.visitor

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import mutation.tool.context.*

/**
 * Visitor which collect contexts from source file
 */
class ContextCatcherJavaVisitor:VoidVisitorAdapter<Any>() {

    /**
     * Collected contexts
     */
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

    override fun visit(n: Parameter?, arg: Any?) {
        super.visit(n, arg)
        if (n != null) contexts.add(ParameterContext(n))
    }
}
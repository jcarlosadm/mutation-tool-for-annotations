package mutation.tool.mutant

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import mutation.tool.operator.Operator

/**
 * Visitor which visits the ast for build the mutant
 *
 * @param operator operator to build the mutant
 * @constructor create a visitor
 */
class MutateVisitor(private val operator:Operator):VoidVisitorAdapter<Any>() {
    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (operator.visit(n, arg)) operator.lock()
    }

    override fun visit(n: MethodDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (operator.visit(n, arg)) operator.lock()
    }

    override fun visit(n: FieldDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (operator.visit(n, arg)) operator.lock()
    }

    override fun visit(n: Parameter?, arg: Any?) {
        super.visit(n, arg)
        if (operator.visit(n, arg)) operator.lock()
    }
}
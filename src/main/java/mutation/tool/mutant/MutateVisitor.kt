package mutation.tool.mutant

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import mutation.tool.operator.Operator

class MutateVisitor(private val operator:Operator):VoidVisitorAdapter<Any>() {
    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)
        operator.visit(n, arg)
    }

    override fun visit(n: MethodDeclaration?, arg: Any?) {
        super.visit(n, arg)
        operator.visit(n, arg)
    }

    override fun visit(n: FieldDeclaration?, arg: Any?) {
        super.visit(n, arg)
        operator.visit(n, arg)
    }

    override fun visit(n: Parameter?, arg: Any?) {
        super.visit(n, arg)
        operator.visit(n, arg)
    }
}
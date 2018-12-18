package mutation.tool.operator

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.annotation.context.Context
import mutation.tool.mutant.Mutant
import java.io.File

fun getValidOperators(context: Context, javaFile: File):List<Operator> {
    TODO("not implemented")
}

abstract class Operator(val context:Context, val file:File) {
    abstract fun checkContext():Boolean
    abstract fun mutate():List<Mutant>
    open fun visit(n:ClassOrInterfaceDeclaration?, arg: Any?) {}
    open fun visit(n:FieldDeclaration?, arg: Any?) {}
    open fun visit(n:MethodDeclaration?, arg: Any?) {}
    open fun visit(n:Parameter?, arg: Any?) {}
}
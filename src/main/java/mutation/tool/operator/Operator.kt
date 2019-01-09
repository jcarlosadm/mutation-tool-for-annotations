package mutation.tool.operator

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.util.MutationToolConfig
import java.io.File

abstract class Operator(val context:Context, val file:File) {
    abstract fun checkContext():Boolean
    abstract fun mutate():List<Mutant>
    open fun visit(n:ClassOrInterfaceDeclaration?, arg: Any?) {}
    open fun visit(n:FieldDeclaration?, arg: Any?) {}
    open fun visit(n:MethodDeclaration?, arg: Any?) {}
    open fun visit(n:Parameter?, arg: Any?) {}
}

fun getValidOperators(contexts: List<Context>, javaFile: File, config: MutationToolConfig):List<Operator> {
    val validOperators = mutableListOf<Operator>()
    val operatorsEnum = config.operators
    val factory = OperatorFactory(config.adaChecker)

    for (operatorEnum in operatorsEnum) validOperators += factory.getOperators(operatorEnum, contexts, javaFile)

    return validOperators
}

enum class OperatorsEnum {
    ADA,
    ADAT,
    CHODR,
    RMA,
    RMAT,
    RPA,
    RPAT,
    RPAV,
    SWTG
}
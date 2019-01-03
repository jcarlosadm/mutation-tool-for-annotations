package mutation.tool.operator

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import java.io.File

abstract class Operator(val context:Context, val file:File) {
    abstract fun checkContext():Boolean
    abstract fun mutate():List<Mutant>
    open fun visit(n:ClassOrInterfaceDeclaration?, arg: Any?) {}
    open fun visit(n:FieldDeclaration?, arg: Any?) {}
    open fun visit(n:MethodDeclaration?, arg: Any?) {}
    open fun visit(n:Parameter?, arg: Any?) {}
}

fun getValidOperators(contexts: List<Context>, javaFile: File, operatorsEnum: List<OperatorsEnum>):List<Operator> {
    val validOperators = mutableListOf<Operator>()

    for (context in contexts) {
        for (operatorEnum in operatorsEnum) {
            val operator = getOperatorInstance(operatorEnum, context, javaFile, contexts)
            if (operator.checkContext())
                validOperators.add(operator)
        }
    }

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

private fun getOperatorInstance(
        operatorEnum: OperatorsEnum,
        context: Context,
        javaFile: File,
        allContexts: List<Context>
): Operator = when(operatorEnum) {
        OperatorsEnum.ADA -> ADA(context, javaFile)
        OperatorsEnum.ADAT -> ADAT(context, javaFile)
        OperatorsEnum.CHODR -> CHODR(context, javaFile)
        OperatorsEnum.RMA -> RMA(context, javaFile)
        OperatorsEnum.RMAT -> RMAT(context, javaFile)
        OperatorsEnum.RPA -> RPA(context, javaFile)
        OperatorsEnum.RPAT -> RPAT(context, javaFile)
        OperatorsEnum.RPAV -> RPAV(context, javaFile)
        OperatorsEnum.SWTG -> SWTG(context, javaFile, allContexts)
    }
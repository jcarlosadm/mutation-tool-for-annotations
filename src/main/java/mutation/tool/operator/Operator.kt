package mutation.tool.operator

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.util.*
import java.io.File

abstract class Operator(val context:Context, val file:File) {
    protected var locked = false
    abstract fun checkContext():Boolean
    abstract fun mutate():List<Mutant>
    open fun visit(n:ClassOrInterfaceDeclaration?, arg: Any?):Boolean = (!locked && n != null && isSameClass(context, n))
    open fun visit(n:FieldDeclaration?, arg: Any?):Boolean = (!locked && n != null && isSameProp(context, n))
    open fun visit(n:MethodDeclaration?, arg: Any?):Boolean = (!locked && n != null && isSameMethod(context, n))
    open fun visit(n:Parameter?, arg: Any?):Boolean = (!locked && n != null && isSameParameter(context, n))
}

fun getValidOperators(contexts: List<Context>, javaFile: File, config: MutationToolConfig):List<Operator> {
    val validOperators = mutableListOf<Operator>()
    val operatorsEnum = config.operators
    val factory = OperatorFactory(config)

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
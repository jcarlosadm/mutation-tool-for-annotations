package mutation.tool.operator

import mutation.tool.annotation.context.Context
import mutation.tool.mutant.Mutant

fun getValidOperators(context:Context):List<Operator> {
    TODO("not implemented")
}

interface Operator {
    fun checkContext(context: Context):Boolean
    fun mutate(context:Context):Mutant?
}
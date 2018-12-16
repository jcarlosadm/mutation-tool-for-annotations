package mutation.tool.operator

import mutation.tool.annotation.context.Context
import mutation.tool.mutant.Mutant

fun getValidOperators(context:Context):List<Operator> {
    TODO("not implemented")
}

abstract class Operator(val context:Context) {

    abstract fun checkContext():Boolean
    abstract fun mutate():List<Mutant>?
}
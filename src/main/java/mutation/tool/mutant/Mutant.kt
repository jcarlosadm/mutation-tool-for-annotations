package mutation.tool.mutant

import mutation.tool.operator.OperatorsEnum

interface Mutant {
    val operator: OperatorsEnum
    override fun toString():String
}
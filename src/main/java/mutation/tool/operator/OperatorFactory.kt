package mutation.tool.operator

import mutation.tool.context.Context
import mutation.tool.operator.checker.ADAChecker
import java.io.File

class OperatorFactory(private val adaChecker: ADAChecker?) {
    fun getOperators(operatorEnum: OperatorsEnum, contexts:List<Context>, file:File):List<Operator> =
        when(operatorEnum) {
            OperatorsEnum.RMA -> this.getRMAOperators(contexts, file)
            OperatorsEnum.RMAT -> this.getRMATOperators(contexts, file)
            OperatorsEnum.ADA -> this.getADAOperators(contexts, file)
            OperatorsEnum.ADAT -> TODO()
            OperatorsEnum.CHODR -> this.getCHODROperators(contexts, file)
            OperatorsEnum.RPA -> TODO()
            OperatorsEnum.RPAT -> TODO()
            OperatorsEnum.RPAV -> TODO()
            OperatorsEnum.SWTG -> TODO()
        }

    private fun getCHODROperators(contexts: List<Context>, file: File): List<Operator> {
        val operators = mutableListOf<Operator>()
        for (context in contexts) {
            val chodr = CHODR(context, file)
            if (chodr.checkContext()) operators += chodr
        }

        return operators
    }

    private fun getADAOperators(contexts: List<Context>, file: File): List<Operator> {
        if (this.adaChecker == null) return listOf()
        return this.adaChecker.check(contexts, file)
    }

    private fun getRMATOperators(contexts: List<Context>, file: File): List<Operator> {
        val operators = mutableListOf<Operator>()
        for (context in contexts) {
            val rmat = RMAT(context, file)
            if (rmat.checkContext()) operators += rmat
        }

        return operators
    }

    private fun getRMAOperators(contexts: List<Context>, file: File):List<Operator> {
        val operators = mutableListOf<Operator>()
        for (context in contexts) {
            val rma = RMA(context, file)
            if (rma.checkContext()) operators += rma
        }

        return operators
    }
}
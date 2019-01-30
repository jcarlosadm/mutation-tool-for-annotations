package mutation.tool.operator

import mutation.tool.context.Context
import mutation.tool.operator.adat.ADAT
import mutation.tool.operator.chodr.CHODR
import mutation.tool.operator.rma.RMA
import mutation.tool.operator.rmat.RMAT
import mutation.tool.operator.rpa.RPA
import mutation.tool.operator.rpat.RPAT
import mutation.tool.operator.swtg.SWTG
import mutation.tool.util.MutationToolConfig
import java.io.File

class OperatorFactory(private val config: MutationToolConfig) {
    fun getOperators(operatorEnum: OperatorsEnum, contexts:List<Context>, file:File):List<Operator> =
        when(operatorEnum) {
            OperatorsEnum.RMA -> this.getRMAOperators(contexts, file)
            OperatorsEnum.RMAT -> this.getRMATOperators(contexts, file)
            OperatorsEnum.ADA -> this.getADAOperators(contexts, file)
            OperatorsEnum.ADAT -> this.getADATOperators(contexts, file)
            OperatorsEnum.CHODR -> this.getCHODROperators(contexts, file)
            OperatorsEnum.RPA -> this.getRPAOperators(contexts, file)
            OperatorsEnum.RPAT -> this.getRPATOperators(contexts, file)
            OperatorsEnum.RPAV -> TODO()
            OperatorsEnum.SWTG -> this.getSWTGOperators(contexts, file)
        }

    private fun getRPATOperators(contexts: List<Context>, file: File): List<Operator> {
        val operators = mutableListOf<Operator>()
        for (context in contexts) {
            val operator = RPAT(context, file)
            operator.map = config.rpatMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getADATOperators(contexts: List<Context>, file: File): List<Operator> {
        val operators = mutableListOf<Operator>()
        for (context in contexts) {
            val operator = ADAT(context, file)
            operator.map = config.adatMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getSWTGOperators(contexts: List<Context>, file: File): List<Operator> {
        val operators = mutableListOf<Operator>()
        for (context in contexts) {
            val operator = SWTG(context, file, contexts)
            operator.mapContextType = config.swtgMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getRPAOperators(contexts: List<Context>, file: File): List<Operator> {
        val operators = mutableListOf<Operator>()
        for (context in contexts) {
            val rpa = RPA(context, file)
            rpa.switchMap = config.rpaMap!!
            rpa.importMap = config.importMap!!
            if (rpa.checkContext()) operators += rpa
        }

        return operators
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
        if (this.config.adaChecker == null) return listOf()
        return this.config.adaChecker!!.check(contexts, file)
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
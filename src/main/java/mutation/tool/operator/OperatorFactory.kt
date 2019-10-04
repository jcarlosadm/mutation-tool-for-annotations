package mutation.tool.operator

import mutation.tool.context.Context
import mutation.tool.operator.adat.JavaADAT
import mutation.tool.operator.chodr.CHODR
import mutation.tool.operator.rma.RMA
import mutation.tool.operator.rmat.RMAT
import mutation.tool.operator.rpa.RPA
import mutation.tool.operator.rpat.RPAT
import mutation.tool.operator.rpav.RPAV
import mutation.tool.operator.swtg.SWTG
import mutation.tool.util.MutationToolConfig
import java.io.File

/**
 * Factory of operators
 * 
 * @param config mutation tool configuration class
 * @constructor create a factory instance
 */
class OperatorFactory(private val config: MutationToolConfig) {

    /**
     * Get a list of operators
     *
     * @param operatorEnum operator type
     * @param contexts contexts of source file
     * @param file source file
     * @return a list of operators
     */
    fun getJavaOperators(operatorEnum: OperatorsEnum, contexts:List<Context>, file:File):List<JavaOperator> {
        return when(operatorEnum) {
            OperatorsEnum.RMA -> this.getJavaRMAOperators(contexts, file)
            OperatorsEnum.RMAT -> this.getJavaRMATOperators(contexts, file)
            OperatorsEnum.ADA -> this.getJavaADAOperators(contexts, file)
            OperatorsEnum.ADAT -> this.getJavaADATOperators(contexts, file)
            OperatorsEnum.CHODR -> this.getJavaCHODROperators(contexts, file)
            OperatorsEnum.RPA -> this.getJavaRPAOperators(contexts, file)
            OperatorsEnum.RPAT -> this.getJavaRPATOperators(contexts, file)
            OperatorsEnum.RPAV -> this.getJavaRPAVOperators(contexts, file)
            OperatorsEnum.SWTG -> this.getJavaSWTGOperators(contexts, file)
        }
    }

    fun getCSharpOperators(operatorEnum: OperatorsEnum, contexts: List<Context>, file: File):List<CSharpOperator> {
        TODO("not implemented")
    }

    private fun getJavaRPAVOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts){
            val operator = RPAV(context, file)
            operator.map = config.rpavMap!!
            if (operator.checkContext()) operators += operator
        }
        
        return operators
    }

    private fun getJavaRPATOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val operator = RPAT(context, file)
            operator.map = config.rpatMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getJavaADATOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val operator = JavaADAT(context, file)
            operator.map = config.adatMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getJavaSWTGOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val operator = SWTG(context, file, contexts)
            operator.mapContextType = config.swtgMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getJavaRPAOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val rpa = RPA(context, file)
            rpa.switchMap = config.rpaMap!!
            if (rpa.checkContext()) operators += rpa
        }

        return operators
    }

    private fun getJavaCHODROperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val chodr = CHODR(context, file)
            if (chodr.checkContext()) operators += chodr
        }

        return operators
    }

    private fun getJavaADAOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()

        if (this.config.adaChecker != null) {
            val list = this.config.adaChecker!!.check(contexts, file)
            for (adaOperator in list) {
                if (adaOperator.checkContext()) operators += adaOperator
            }
        }

        return operators
    }

    private fun getJavaRMATOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val rmat = RMAT(context, file)
            if (rmat.checkContext()) operators += rmat
        }

        return operators
    }

    private fun getJavaRMAOperators(contexts: List<Context>, file: File):List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val rma = RMA(context, file)
            if (rma.checkContext()) operators += rma
        }

        return operators
    }
}
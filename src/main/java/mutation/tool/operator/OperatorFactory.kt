package mutation.tool.operator

import mutation.tool.context.Context
import mutation.tool.operator.adat.CSharpADAT
import mutation.tool.operator.adat.JavaADAT
import mutation.tool.operator.chodr.CSharpCHODR
import mutation.tool.operator.chodr.JavaCHODR
import mutation.tool.operator.rma.CSharpRMA
import mutation.tool.operator.rma.JavaRMA
import mutation.tool.operator.rmat.CSharpRMAT
import mutation.tool.operator.rmat.JavaRMAT
import mutation.tool.operator.rpa.CSharpRPA
import mutation.tool.operator.rpa.JavaRPA
import mutation.tool.operator.rpat.CSharpRPAT
import mutation.tool.operator.rpat.JavaRPAT
import mutation.tool.operator.rpav.CSharpRPAV
import mutation.tool.operator.rpav.JavaRPAV
import mutation.tool.operator.swtg.CSharpSWTG
import mutation.tool.operator.swtg.JavaSWTG
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
        return when(operatorEnum) {
            OperatorsEnum.ADA -> this.getCSharpADAOperators(contexts, file)
            OperatorsEnum.ADAT -> this.getCSharpADATOperators(contexts, file)
            OperatorsEnum.CHODR -> this.getCSharpCHODROperators(contexts, file)
            OperatorsEnum.RMA -> this.getCSharpRMAOperators(contexts, file)
            OperatorsEnum.RMAT -> this.getCSharpRMATOperators(contexts, file)
            OperatorsEnum.RPA -> this.getCSharpRPAOperators(contexts, file)
            OperatorsEnum.RPAT -> this.getCSharpRPATOperators(contexts, file)
            OperatorsEnum.RPAV -> this.getCSharpRPAVOperators(contexts, file)
            OperatorsEnum.SWTG -> this.getCSharpSWTGOperators(contexts, file)
        }
    }

    private fun getCSharpRPAVOperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()
        for (context in contexts){
            val operator = CSharpRPAV(context, file)
            operator.map = config.rpavMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getJavaRPAVOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts){
            val operator = JavaRPAV(context, file)
            operator.map = config.rpavMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getCSharpRPATOperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()
        for (context in contexts) {
            val operator = CSharpRPAT(context, file)
            operator.map = config.rpatMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getJavaRPATOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val operator = JavaRPAT(context, file)
            operator.map = config.rpatMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getCSharpADATOperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()
        for (context in contexts) {
            val operator = CSharpADAT(context, file)
            operator.map = config.adatMap!!
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

    private fun getCSharpSWTGOperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()
        for (context in contexts) {
            val operator = CSharpSWTG(context, file, contexts)
            operator.mapContextType = config.swtgMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getJavaSWTGOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val operator = JavaSWTG(context, file, contexts)
            operator.mapContextType = config.swtgMap!!
            if (operator.checkContext()) operators += operator
        }

        return operators
    }

    private fun getCSharpRPAOperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()
        for (context in contexts) {
            val rpa = CSharpRPA(context, file)
            rpa.switchMap = config.rpaMap!!
            if (rpa.checkContext()) operators += rpa
        }

        return operators
    }

    private fun getJavaRPAOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val rpa = JavaRPA(context, file)
            rpa.switchMap = config.rpaMap!!
            if (rpa.checkContext()) operators += rpa
        }

        return operators
    }

    private fun getCSharpCHODROperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()
        for (context in contexts) {
            val chodr = CSharpCHODR(context, file)
            if (chodr.checkContext()) operators += chodr
        }

        return operators
    }

    private fun getJavaCHODROperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val chodr = JavaCHODR(context, file)
            if (chodr.checkContext()) operators += chodr
        }

        return operators
    }

    private fun getCSharpADAOperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()

        if (this.config.adaChecker != null) {
            val list = this.config.adaChecker!!.checkCSharp(contexts, file)
            for (adaOperator in list) {
                if (adaOperator.checkContext()) operators += adaOperator
            }
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

    private fun getCSharpRMATOperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()
        for (context in contexts) {
            val rmat = CSharpRMAT(context, file)
            if (rmat.checkContext()) operators += rmat
        }

        return operators
    }

    private fun getJavaRMATOperators(contexts: List<Context>, file: File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val rmat = JavaRMAT(context, file)
            if (rmat.checkContext()) operators += rmat
        }

        return operators
    }

    private fun getCSharpRMAOperators(contexts: List<Context>, file: File): List<CSharpOperator> {
        val operators = mutableListOf<CSharpOperator>()
        for (context in contexts) {
            val rma = CSharpRMA(context, file)
            if (rma.checkContext()) operators += rma
        }

        return operators
    }

    private fun getJavaRMAOperators(contexts: List<Context>, file: File):List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()
        for (context in contexts) {
            val rma = JavaRMA(context, file)
            if (rma.checkContext()) operators += rma
        }

        return operators
    }
}
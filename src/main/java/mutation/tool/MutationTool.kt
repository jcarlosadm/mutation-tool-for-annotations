package mutation.tool

import mu.KotlinLogging
import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.CSharpStrategy
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.mutant.generateJavaMutants
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.ada.ADAChecker
import mutation.tool.operator.adat.ADATMapBuilder
import mutation.tool.operator.getValidJavaOperators
import mutation.tool.operator.rpa.RPAMapBuilder
import mutation.tool.operator.rpat.RPATMapBuilder
import mutation.tool.operator.rpav.RPAVMapBuilder
import mutation.tool.operator.swtg.SWTGMapBuilder
import mutation.tool.project.Project
import mutation.tool.util.*
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

private val logger = KotlinLogging.logger{}
private const val TOOL_NAME = "Mutation Tool for Annotations"

/**
 * class to run mutation tool
 * @param config: configuration class
 */
class MutationTool(private val config: MutationToolConfig) {
    private var project:Project? = null

    /**
     * Run mutation tool
     */
    fun run():Boolean {
        logger.info { "\n\n	#### Starting $TOOL_NAME #### \n" }

        try {
            this.init()

            if (config.testOriginalProject)
                this.testOriginalProject()

            this.genMutants()

            if (config.testMutants)
                this.testMutants()

            this.end()
        } catch (e:Exception){
            logger.error(e) {"${e.message}"}
            return false
        }

        return true
    }

    private fun init() {
        logger.info { "checking source and test directories..." }
        if (!config.pathSources.exists() || !config.pathSources.isDirectory) {
            throw IOException("following source folder not exists or isn't a directory: ${config.pathSources}")
        }

        if ((config.testMutants || config.testOriginalProject) && (!config.pathTests.exists() ||
                !config.pathTests.isDirectory)) {
            throw IOException("following test folder not exists or isn't a directory: ${config.pathTests}")
        }

        if ((config.testMutants || config.testOriginalProject) && isSubFolder(config.pathSources, config.pathTests)) {
            throw Exception("one folder is subfolder of another folder. exiting...")
        }

        logger.info { "source and test directories: OK" }

        project = Project(config.projectName,config.pathSources)

        logger.info { "creating basic directories..." }
        if (!makeRootFolders()) throw ExceptionInInitializerError("Error to make root folders")
        logger.info { "creating basic directories: done" }

        this.setADAChecker(config)
        this.setSWTGMap(config)
        this.setRPAMap(config)
        this.setADATMap(config)
        this.setRPATMap(config)
        this.setRPAVMap(config)
    }

    private fun testOriginalProject() {
        logger.info { "Running original project against test suite" }

        if (this.project?.runTests(config.pathTests) == false)
            throw Exception("original project fail against test suite. exiting...")
    }

    private fun genMutants() {
        logger.info { "generating mutants" }
        val executor = Executors.newFixedThreadPool(config.threads)

        val validFiles = when(config.language) {
            Language.JAVA -> getAllJavaFiles(config.pathSources)
            Language.C_SHARP -> getAllCSharpFiles(config.pathSources)
        }

        for(validFile in validFiles){
            val worker = Runnable {
                synchronized(this) { logger.info { "check source file: $validFile" } }

                val visitor = when(config.language) {
                    Language.JAVA -> JavaStrategy()
                    Language.C_SHARP -> CSharpStrategy()
                }

                val operators = getValidJavaOperators(getListOfAnnotationContext(validFile, visitor), validFile, config)
                generateJavaMutants(operators, validFile, project!!, File(config.mutantsFolder))

                synchronized(this) { logger.info { "source file checked: $validFile" } }
            }
            executor.execute(worker)
        }

        executor.shutdown()
        while (!executor.isTerminated){ Thread.sleep(100) }

        logger.info { "generation of mutants ended" }
    }

    private fun setADAChecker(config: MutationToolConfig) {
        if (config.operators.contains(OperatorsEnum.ADA)) {
            config.adaChecker = ADAChecker(config.annotationInfos)
            config.adaChecker!!.build()
        }
    }

    private fun setSWTGMap(config: MutationToolConfig) {
        val builder = SWTGMapBuilder(config.annotationInfos)
        builder.build()
        config.swtgMap = builder.map
    }

    private fun setRPAMap(config: MutationToolConfig) {
        val builder = RPAMapBuilder(config.annotationInfos)
        builder.build()
        config.rpaMap = builder.map
    }

    private fun setADATMap(config: MutationToolConfig) {
        val builder = ADATMapBuilder(config.annotationInfos)
        builder.build()
        config.adatMap = builder.map
    }

    private fun setRPATMap(config: MutationToolConfig) {
        val builder = RPATMapBuilder(config.annotationInfos)
        builder.build()
        config.rpatMap = builder.map
    }

    private fun setRPAVMap(config: MutationToolConfig) {
        val builder = RPAVMapBuilder(config.annotationInfos)
        builder.build()
        config.rpavMap = builder.map
    }

    private fun testMutants() {
        TODO("not implemented")
    }

    private fun end() {
        if (!deleteTempFolder()) throw Exception("Error to delete temporary folder")
    }
}
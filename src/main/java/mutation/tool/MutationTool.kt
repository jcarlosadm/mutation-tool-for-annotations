package mutation.tool

import mu.KotlinLogging
import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.mutant.generateMutants
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.ada.ADAChecker
import mutation.tool.operator.getValidOperators
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
    }

    private fun testOriginalProject() {
        logger.info { "Running original project against test suite" }

        if (this.project?.runTests(config.pathTests) == false)
            throw Exception("original project fail against test suite. exiting...")
    }

    private fun genMutants() {
        logger.info { "generating mutants" }
        val executor = Executors.newFixedThreadPool(config.threads)

        for(javaFile in getAllJavaFiles(config.pathSources)){

            val worker = Runnable {
                synchronized(this) { logger.info { "check java file: $javaFile" } }

                val operators = getValidOperators(getListOfAnnotationContext(javaFile), javaFile, config)
                generateMutants(operators, javaFile, project!!, File(config.mutantsFolder))

                synchronized(this) { logger.info { "java file checked: $javaFile" } }
            }
            executor.execute(worker)
        }

        executor.shutdown()
        while (!executor.isTerminated){}

        logger.info { "generation of mutants ended" }
    }

    private fun setADAChecker(config: MutationToolConfig) {
        if (config.operators.contains(OperatorsEnum.ADA)){
            config.adaChecker = ADAChecker()
            config.adaChecker?.buildTree()
        }
    }

    private fun testMutants() {
        TODO("not implemented")
    }

    private fun end() {
        if (!deleteTempFolder()) throw Exception("Error to delete temporary folder")
    }
}
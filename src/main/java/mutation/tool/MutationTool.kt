package mutation.tool

import mutation.tool.util.MutationToolConfig
import mutation.tool.project.Project
import java.io.File
import mu.KotlinLogging
import mutation.tool.util.deleteTempFolder
import mutation.tool.util.makeRootFolders
import java.lang.Exception

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
    fun run() {
        logger.info { "\n\n	#### Starting $TOOL_NAME #### \n" }

        try {
            this.init()
            this.testOriginalProject()
            this.end()
        } catch (e:Exception){
            logger.error(e) {"${e.message}"}
            return
        }
    }

    private fun init() {
        // TODO: check if source and test folders exist and both not intersects with each other

        // load original project
        project = Project(File(config.pathSources))

        if (!makeRootFolders()) throw ExceptionInInitializerError("Error to make root folders")
    }

    private fun testOriginalProject() {
        logger.info { "Running original project against test suite" }

        if (this.project?.runTests(config.pathTests) == false)
            throw Exception("original project fail against test suite. exiting...")
    }

    private fun end() {
        if (!deleteTempFolder()) throw Exception("Error to delete temporary folder")
    }
}

fun main(args: Array<String>) {
    val config = MutationToolConfig("", "")
    MutationTool(config).run()
}
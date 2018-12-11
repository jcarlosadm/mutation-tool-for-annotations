package mutation.tool

import mutation.tool.util.MutationToolConfig
import mutation.tool.project.Project
import java.io.File
import mu.KotlinLogging
import mutation.tool.util.deleteTempFolder
import mutation.tool.util.foldersIntersects
import mutation.tool.util.makeRootFolders
import java.io.IOException
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
    fun run():Boolean {
        logger.info { "\n\n	#### Starting $TOOL_NAME #### \n" }

        try {
            this.init()
            this.testOriginalProject()
            this.genMutants()
            this.end()
        } catch (e:Exception){
            logger.error(e) {"${e.message}"}
            return false
        }

        return true
    }

    private fun init() {
        if (!config.pathSources.exists() || !config.pathSources.isDirectory) {
            throw IOException("source folder not exists or isn't a directory")
        }

        if (!config.pathTests.exists() || !config.pathTests.isDirectory) {
            throw IOException("test folder not exists or isn't a directory")
        }

        if (foldersIntersects(config.pathSources, config.pathTests)) {
            throw Exception("exists intersection between source and test folders. exiting...")
        }

        project = Project(config.pathSources)

        if (!makeRootFolders()) throw ExceptionInInitializerError("Error to make root folders")
    }

    private fun testOriginalProject() {
        logger.info { "Running original project against test suite" }

        if (this.project?.runTests(config.pathTests) == false)
            throw Exception("original project fail against test suite. exiting...")
    }

    private fun genMutants() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun end() {
        if (!deleteTempFolder()) throw Exception("Error to delete temporary folder")
    }
}

fun main(args: Array<String>) {
    val config = MutationToolConfig(File(""), File(""))
    MutationTool(config).run()
}
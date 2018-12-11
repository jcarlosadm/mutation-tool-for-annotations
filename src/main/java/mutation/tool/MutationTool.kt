package mutation.tool

import mutation.tool.util.MutationToolConfig
import mutation.tool.project.Project
import java.io.File
import mu.KotlinLogging

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

        this.init()

        logger.info { "Running original project against test suite" }
        if (project?.runTests(config.pathTests) == false){
            logger.error { "original project fail against test suite. exiting..." }
            return
        }
    }

    /**
     * load classes and make basic checks
     */
    private fun init(){
        // TODO: check if source and test folders exist and both not intersects with each other

        // load original project
        project = Project(File(config.pathSources))
    }
}

fun main(args: Array<String>) {
    val config = MutationToolConfig("", "")
    MutationTool(config).run()
}
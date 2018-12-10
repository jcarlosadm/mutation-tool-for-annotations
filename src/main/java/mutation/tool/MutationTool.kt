package mutation.tool

import mutation.tool.util.MutationToolConfig
import mutation.tool.project.Project
import java.io.File
import mu.KotlinLogging

private val logger = KotlinLogging.logger{}
private val TOOL_NAME = "Mutation Tool for Annotations"

class MutationTool(config: MutationToolConfig) {
	val config = config

	fun run() {
		logger.info { "\n\n	#### Starting $TOOL_NAME #### \n" }
		
		val project = Project(File(config.pathSources))
		
		logger.info { "Running original project against test suite" }
		if (project.runTests(config.pathTests) == false){
			logger.error { "original project fail against test suite. exiting..." }
			return
		}
	}
}

fun main(args: Array<String>) {
	val config = MutationToolConfig("", "")
	MutationTool(config).run()
}
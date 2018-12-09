package mutation.tool

import mutation.tool.util.MutationToolConfig
import mutation.tool.project.Project
import java.io.File

class MutationTool(config: MutationToolConfig) {
	val config = config

	fun run() {
		val project = Project(File(config.pathSources))
		
		// run original project against tests
		if (project.runTests(config.pathTests) == false){
			println("original project fail on tests. It doesn't seem right... exiting...")
			return
		}
	}
}
package mutation.tool.util

class MutationToolConfig(pathSources: String, pathTests: String) {
	var threads = 1
		set(value) {
			if (value > 0) threads = value
		}
	val pathSources = pathSources
	val pathTests = pathTests
}
package mutation.tool.util

import java.io.File

class MutationToolConfig(val pathSources: File, val pathTests: File) {
	var threads = 1
		set(value) {
			if (value > 0) threads = value
		}
}
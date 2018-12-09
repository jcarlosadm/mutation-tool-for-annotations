package mutation.tool.project

import java.io.File

open class Project(folder:File) {
	val folder = folder
	
	fun runTests():Boolean {
		return true
	}
}
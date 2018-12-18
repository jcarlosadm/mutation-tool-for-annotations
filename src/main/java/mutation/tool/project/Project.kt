package mutation.tool.project

import java.io.File

open class Project(val name:String, private val folder: File) {

	fun runTests(pathTests:File):Boolean {
		TODO("not implemented")
	}

	override fun toString(): String {
		return this.name
	}
}
package mutation.tool.project

import java.io.File

/**
 * Information about the original project to be mutated
 * 
 * @param name name of the project
 * @param folder path of the project
 */
open class Project(val name:String, private val folder: File) {

	/**
	 * run tests
	 *
	 * @param pathTests path of the tests
	 * @return false if at least one test fail. true otherwise.
	 */
	fun runTests(pathTests:File):Boolean {
		TODO("not implemented")
	}

	override fun toString(): String {
		return this.name
	}
}
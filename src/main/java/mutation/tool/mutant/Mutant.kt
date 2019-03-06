package mutation.tool.mutant

import com.github.javaparser.ast.CompilationUnit
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.project.Project
import java.io.File

private const val PATTERN = "%07d"

private var mutantNum = 0

/**
 * Generate mutants
 *
 * @param operators list of operators
 * @param javaFile source file
 * @param project information about the original project
 * @param mutantFolder Directory where the mutants are generated
 */
fun generateMutants(operators: List<Operator>, javaFile: File, project: Project, mutantFolder:File) {
	for (operator in operators) {
		val mutants = operator.mutate()
		for (mutant in mutants) {
			val path = "$mutantFolder/${mutant.operator.name}/${getNum()}"
			File(path).mkdirs()
			File("$path/${javaFile.name}").writeText(mutant.toString())

			File("$path/info.json").bufferedWriter().use { out ->
				out.write("{")
				out.newLine()
				out.write("		\"projectName\": \"$project\",")
				out.newLine()
				out.write("		\"originalFile\": \"${javaFile.path}\",")
				out.newLine()
				out.write("		\"operator\": \"${mutant.operator.name}\",")
				out.newLine()
				out.write("		\"before\": \"${mutant.before}\",")
				out.newLine()
				out.write("		\"after\": \"${mutant.after}\"")
				out.newLine()
				out.write("}")
			}
		}
	}
}

/**
 * Mutant class
 *
 * @param operator operator of this mutant
 * @constructor creates a mutant
 */
class Mutant(val operator:OperatorsEnum) {
	/**
	 * Structure of changed file
	 */
	lateinit var compilationUnit: CompilationUnit

	/**
	 * String of affected location before the changes
	 */
	var before:String = ""

	/**
	 * String of affected location after the changes
	 */
	var after:String = ""

	/**
	 * String representation of changed file
	 */
	override fun toString(): String = compilationUnit.toString()
}

@Synchronized
private fun getNum(): String = PATTERN.format(++mutantNum)

/**
 * reset the folder counter
 */
@Synchronized
fun resetMutantFoldersNum() { mutantNum = 0 }
package mutation.tool.mutant

import com.github.javaparser.ast.CompilationUnit
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.project.Project
import java.io.File

private const val PATTERN = "%07d"

private var mutantNum = 0

fun generateMutants(operators: List<Operator>, javaFile: File, project: Project, mutantFolder:File) {
	for (operator in operators) {
		val mutants = operator.mutate()
		for (mutant in mutants) {
			val path = "$mutantFolder/${getNum()}"
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

class Mutant(val operator:OperatorsEnum) {
	lateinit var compilationUnit: CompilationUnit
	var before:String = ""
	var after:String = ""

	override fun toString(): String = compilationUnit.toString()
}

@Synchronized
private fun getNum(): String = PATTERN.format(++mutantNum)

@Synchronized
fun resetMutantFoldersNum() { mutantNum = 0 }
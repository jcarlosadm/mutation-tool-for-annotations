package mutation.tool.mutant

import com.github.javaparser.Range
import com.github.javaparser.ast.CompilationUnit
import mutation.tool.operator.Operator
import mutation.tool.project.Project
import mutation.tool.util.MUTANTS_FOLDER
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
				out.write("		\"originalFile\": \"${javaFile.path}\"")
				out.newLine()
				out.write("}")
			}
		}
	}
}

class Mutant(private val compilationUnit: CompilationUnit) {
	override fun toString(): String {
		return compilationUnit.toString()
	}
}

@Synchronized
private fun getNum(): String = PATTERN.format(++mutantNum)
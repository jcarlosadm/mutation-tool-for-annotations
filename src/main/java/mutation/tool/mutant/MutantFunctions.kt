package mutation.tool.mutant

import mutation.tool.operator.Operator
import mutation.tool.project.Project
import org.json.JSONObject
import java.io.BufferedWriter
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
			File("$path/info.json").bufferedWriter().use { out -> writeJson(out, project, javaFile, mutant) }
		}
	}

	File("$mutantFolder/info.json").bufferedWriter().use { out -> writeReport(out) }
}

private fun writeReport(out: BufferedWriter) {
	val json = JSONObject()

	json.put("number of mutants", mutantNum)
	out.write(json.toString(4))
}

private fun writeJson(out:BufferedWriter, project:Project, javaFile: File, javaMutant: JavaMutant) {
    val json = JSONObject()

    json.put("projectName", project.name)
    json.put("originalFile", javaFile.path)
    json.put("operator", javaMutant.operator.name)

    out.write(json.toString(4))
}

@Synchronized
private fun getNum(): String = PATTERN.format(++mutantNum)

/**
 * reset the folder counter
 */
@Synchronized
fun resetMutantFoldersNum() { mutantNum = 0 }
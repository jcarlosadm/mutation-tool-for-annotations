package mutation.tool.mutant

import mutation.tool.operator.Operator
import mutation.tool.project.Project
import java.io.File

fun generateMutants(operators:List<Operator>, javaFile:File) {
	TODO("not implemented")
}

class Mutant(folder:File):Project(folder) {
	private val mutatedFiles = mutableListOf<MutatedFile>()
}
package mutation.tool.mutant

import com.github.javaparser.Range
import com.github.javaparser.ast.CompilationUnit
import mutation.tool.operator.Operator
import mutation.tool.project.Project
import java.io.File

fun generateMutants(operators: List<Operator>, javaFile: File, project: Project?) {
	TODO("not implemented")
}

class Mutant(private val compilationUnit: CompilationUnit) {
	override fun toString(): String {
		return compilationUnit.toString()
	}
}
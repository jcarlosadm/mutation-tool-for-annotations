package mutation.tool.mutant

import com.github.javaparser.ast.CompilationUnit
import mutation.tool.operator.OperatorsEnum

/**
 * Mutant class
 *
 * @param operator operator of this mutant
 * @constructor creates a mutant
 */
class JavaMutant(override val operator: OperatorsEnum):Mutant {
	/**
	 * Structure of changed file
	 */
	lateinit var compilationUnit: CompilationUnit

	/**
	 * String representation of changed file
	 */
	override fun toString(): String = compilationUnit.toString()
}
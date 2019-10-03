package mutation.tool.operator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.Context
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.JavaMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.util.*
import org.w3c.dom.Node
import java.io.File

// TODO not implemented
abstract class CSharpOperator {
    private val mutateVisitor = CSharpMutateVisitor(this)
    // TODO private val rootNode = ?
    private var locked = false

    /**
     * Check if this operator can be applicable to context
     * @return true if can be aplicable
     */
    abstract fun checkContext():Boolean

    /**
     * Generate mutants
     *
     * @return a list of mutants
     */
    abstract fun mutate():List<CSharpMutant>

    /**
     * Visit and change the ast of the source file
     *
     * @return the changed compilation unit (representation of the ast)
     */
    protected fun visit(): Node {
        val newCompUnit = compUnit.clone()
        this.unlock()
        mutateVisitor.visit(newCompUnit, null)
        return newCompUnit
    }

    /**
     * Method activated when a class or interface declaration is visited
     *
     * @param n instance of class or interface declaration
     * @param arg additional arguments
     * @return true if desired actions are performed with success
     */
    open fun visitClass(node:Node):Boolean = (!locked && n != null && isSameClass(context, n))

    /**
     * Method activated when a field declaration is visited
     *
     * @param n instance of field
     * @param arg additional arguments
     * @return true if desired actions are performed with success
     */
    open fun visitProperty(node:Node):Boolean = (!locked && n != null && isSameProp(context, n))

    /**
     * Method activated when a method declaration is visited
     *
     * @param n instance of method declaration
     * @param arg additional arguments
     * @return true if desired actions are performed with success
     */
    open fun visitMethod(node:Node):Boolean = (!locked && n != null && isSameMethod(context, n))

    /**
     * Method activated when a parameter declaration is visited
     *
     * @param n instance of parameter declaration
     * @param arg additional arguments
     * @return true if desired actions are performed with success
     */
    open fun visitParameter(node:Node):Boolean = (!locked && n != null && isSameParameter(context, n))

    /**
     * allow actions when visiting an ast
     */
    open fun unlock() { locked = false }

    /**
     * preventing actions when visiting an ast
     */
    open fun lock() { locked = true }
}

/**
 * Get all valid operators on basis of list of contexts and configurations
 *
 * @param contexts list of contexts
 * @param cSharpFile source file
 * @param config configuration
 * @return list of operators
 */
fun getValidCSharpOperators(contexts: List<Context>, cSharpFile: File, config: MutationToolConfig):List<CSharpOperator> {
    val validOperators = mutableListOf<CSharpOperator>()
    val operatorsEnum = config.operators
    val factory = OperatorFactory(config)

    for (operatorEnum in operatorsEnum) validOperators += factory.getCSharpOperators(operatorEnum, contexts, cSharpFile)

    return validOperators
}
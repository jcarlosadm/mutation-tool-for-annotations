package mutation.tool.operator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.mutant.JavaMutateVisitor
import mutation.tool.util.*
import java.io.File

/**
 * Base of operators
 *
 * @param context context of this operator
 * @param file source file
 * @constructor create an Operator instance
 */
abstract class JavaOperator(val context:Context, val file:File) {
    @Suppress("LeakingThis")
    private val mutateVisitor = JavaMutateVisitor(this)
    private val compUnit = JavaParser.parse(file)
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
    abstract fun mutate():List<JavaMutant>

    /**
     * Visit and change the ast of the source file
     *
     * @return the changed compilation unit (representation of the ast)
     */
    protected fun visit():CompilationUnit {
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
    open fun visit(n:ClassOrInterfaceDeclaration?, arg: Any?):Boolean = (!locked && n != null && isSameClass(context, n))

    /**
     * Method activated when a field declaration is visited
     *
     * @param n instance of field
     * @param arg additional arguments
     * @return true if desired actions are performed with success
     */
    open fun visit(n:FieldDeclaration?, arg: Any?):Boolean = (!locked && n != null && isSameProp(context, n))

    /**
     * Method activated when a method declaration is visited
     *
     * @param n instance of method declaration
     * @param arg additional arguments
     * @return true if desired actions are performed with success
     */
    open fun visit(n:MethodDeclaration?, arg: Any?):Boolean = (!locked && n != null && isSameMethod(context, n))

    /**
     * Method activated when a parameter declaration is visited
     *
     * @param n instance of parameter declaration
     * @param arg additional arguments
     * @return true if desired actions are performed with success
     */
    open fun visit(n:Parameter?, arg: Any?):Boolean = (!locked && n != null && isSameParameter(context, n))

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
 * @param javaFile source file
 * @param config configuration
 * @return list of operators
 */
fun getValidJavaOperators(contexts: List<Context>, javaFile: File, config: MutationToolConfig):List<JavaOperator> {
    val validOperators = mutableListOf<JavaOperator>()
    val operatorsEnum = config.operators
    val factory = OperatorFactory(config)

    for (operatorEnum in operatorsEnum) validOperators += factory.getJavaOperators(operatorEnum, contexts, javaFile)

    return validOperators
}


package mutation.tool.operator.rma

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mu.KotlinLogging
import mutation.tool.context.*
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.*
import java.io.File

private val logger = KotlinLogging.logger{}

/**
 * Remove Annotation Operator
 */
class RMA(context: Context, file: File) : Operator(context, file) {

    private var currentMutant:Mutant? = null
    private var currentAnnotation:AnnotationExpr? = null

    override fun checkContext(): Boolean = (getAnnotations(context)).isNotEmpty()

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        val mutateVisitor = MutateVisitor(this)
        val compUnit = JavaParser.parse(file)

        for (annotation in getAnnotations(context)) {
            val newCompUnit = compUnit.clone()
            currentAnnotation = annotation
            locked = false
            currentMutant = Mutant(OperatorsEnum.RMA)
            mutateVisitor.visit(newCompUnit, null)
            logger.debug { "$newCompUnit" }
            currentMutant?.compilationUnit = newCompUnit
            mutants.add(currentMutant!!)
        }

        return mutants
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?):Boolean = super.visit(n, arg) &&
            removeCurrentAnnotation(n!!.annotations)

    override fun visit(n: FieldDeclaration?, arg: Any?):Boolean = super.visit(n, arg) &&
            removeCurrentAnnotation(n!!.annotations)

    override fun visit(n: MethodDeclaration?, arg: Any?):Boolean = super.visit(n, arg) &&
            removeCurrentAnnotation(n!!.annotations)

    override fun visit(n: Parameter?, arg: Any?):Boolean = super.visit(n, arg) &&
            removeCurrentAnnotation(n!!.annotations)

    private fun removeCurrentAnnotation(annotations:List<AnnotationExpr>): Boolean {
        if (currentAnnotation == null)
            return false

        for (annotation in annotations) {
            if (annotation.toString() == currentAnnotation?.toString()) {
                currentMutant?.before = annotation.toString()
                annotation.remove()
                locked = true
                return true
            }
        }

        return false
    }
}

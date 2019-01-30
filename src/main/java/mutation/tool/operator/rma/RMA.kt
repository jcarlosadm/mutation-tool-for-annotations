package mutation.tool.operator.rma

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import java.io.File

/**
 * Remove Annotation Operator
 */
class RMA(context: Context, file: File) : Operator(context, file) {
    private var currentMutant:Mutant? = null
    private var currentAnnotation:AnnotationExpr? = null

    override fun checkContext(): Boolean = (context.getAnnotations()).isNotEmpty()

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        for (annotation in context.getAnnotations()) {
            currentAnnotation = annotation
            currentMutant = Mutant(OperatorsEnum.RMA)
            currentMutant?.compilationUnit = this.visit()
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
                return true
            }
        }

        return false
    }
}

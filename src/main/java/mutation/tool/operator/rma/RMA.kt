package mutation.tool.operator.rma

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.builder.AnnotationBuilder
import mutation.tool.annotation.builder.JavaAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.mutant.JavaMutateVisitor
import mutation.tool.operator.JavaOperator
import mutation.tool.operator.OperatorsEnum
import java.io.File

/**
 * Remove Annotation Operator
 *
 * @param context context of this operator
 * @param file source file
 * @constructor create a RMA operator
 */
class RMA(context: Context, file: File) : JavaOperator(context, file) {
    override val mutateVisitor = JavaMutateVisitor(this)
    private var currentJavaMutant:JavaMutant? = null
    private var currentAnnotation:AnnotationExpr? = null

    override fun checkContext(): Boolean = (context.annotations).isNotEmpty()

    override fun mutate(): List<JavaMutant> {
        val mutants = mutableListOf<JavaMutant>()

        for (annotation in context.annotations) {
            val builder = JavaAnnotationBuilder(annotation.string)
            builder.build()
            currentAnnotation = builder.annotationExpr!!
            currentJavaMutant = JavaMutant(OperatorsEnum.RMA)
            currentJavaMutant?.compilationUnit = this.visit()
            mutants.add(currentJavaMutant!!)
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
                annotation.remove()
                return true
            }
        }

        return false
    }
}

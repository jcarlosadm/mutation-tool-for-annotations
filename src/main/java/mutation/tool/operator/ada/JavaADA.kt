package mutation.tool.operator.ada

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.annotation.builder.JavaAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.JavaOperator
import mutation.tool.operator.OperatorsEnum
import java.io.File

/**
 * Add Annotation
 *
 * @param context context of this operator
 * @param file source file
 * @constructor create a ADA operator
 */
class JavaADA(context: Context, file:File): JavaOperator(context, file) {

    /**
     * annotation of this operator
     */
    var annotation:String? = null

    /**
     * temporary mutant
     */
    var mutant = JavaMutant(OperatorsEnum.ADA)

    override fun checkContext(): Boolean {
        if (annotation == null) return false

        for (annotationContext in context.annotations) {
            if (getName(annotation!!) == annotationContext.name) return false
        }

        return true
    }

    private fun getName(annotation: String): String? {
        if (annotation.contains("("))
            return annotation.substring(0, annotation.indexOf("(")).removePrefix("@")
        return annotation.removePrefix("@")
    }

    override fun mutate(): List<JavaMutant> {
        if (annotation == null) throw Exception("ADA with null annotation")
        mutant.compilationUnit = this.visit()
        return listOf(mutant)
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) : Boolean = super.visit(n, arg) &&
        insertAnnotation(n, null, null, null)

    override fun visit(n: MethodDeclaration?, arg: Any?) : Boolean = super.visit(n, arg) &&
        insertAnnotation(null, n, null, null)

    override fun visit(n: FieldDeclaration?, arg: Any?) : Boolean = super.visit(n, arg) &&
        insertAnnotation(null, null, n, null)

    override fun visit(n: Parameter?, arg: Any?) : Boolean = super.visit(n, arg) &&
        insertAnnotation(null, null, null, n)

    private fun insertAnnotation(
            classOrInterfaceDeclaration: ClassOrInterfaceDeclaration?,
            methodDeclaration: MethodDeclaration?,
            fieldDeclaration: FieldDeclaration?,
            parameter: Parameter?
    ):Boolean {
        val annotationBuilder = JavaAnnotationBuilder(annotation!!)
        annotationBuilder.build()
        when {
            classOrInterfaceDeclaration != null -> classOrInterfaceDeclaration.
                    addAnnotation(annotationBuilder.annotationExpr)
            methodDeclaration != null -> methodDeclaration.addAnnotation(annotationBuilder.annotationExpr)
            fieldDeclaration != null -> fieldDeclaration.addAnnotation(annotationBuilder.annotationExpr)
            parameter != null -> parameter.addAnnotation(annotationBuilder.annotationExpr)
            else -> return false
        }

        return true
    }
}
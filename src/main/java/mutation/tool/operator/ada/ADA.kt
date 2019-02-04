package mutation.tool.operator.ada

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.annotation.AnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import java.io.File

/**
 * Add Annotation
 */
class ADA(context: Context, file:File): Operator(context, file) {
    var annotation:String? = null
    var mutant = Mutant(OperatorsEnum.ADA)

    override fun checkContext(): Boolean {
        if (annotation == null) return false

        for (annotationContext in context.getAnnotations()) {
            if (getName(annotation!!) == annotationContext.nameAsString) return false
        }

        return true
    }

    private fun getName(annotation: String): String? {
        if (annotation.contains("("))
            return annotation.substring(0, annotation.indexOf("(")).removePrefix("@")
        return annotation.removePrefix("@")
    }

    override fun mutate(): List<Mutant> {
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
        when {
            classOrInterfaceDeclaration != null -> classOrInterfaceDeclaration.
                    addAnnotation(AnnotationBuilder(annotation!!).build())
            methodDeclaration != null -> methodDeclaration.addAnnotation(AnnotationBuilder(annotation!!).build())
            fieldDeclaration != null -> fieldDeclaration.addAnnotation(AnnotationBuilder(annotation!!).build())
            parameter != null -> parameter.addAnnotation(AnnotationBuilder(annotation!!).build())
            else -> return false
        }

        mutant.after = annotation!!

        return true
    }
}
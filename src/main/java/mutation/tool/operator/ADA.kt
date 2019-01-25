package mutation.tool.operator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.annotation.AnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.util.isSameClass
import mutation.tool.util.isSameMethod
import mutation.tool.util.isSameParameter
import mutation.tool.util.isSameProp
import java.io.File

/**
 * Add Annotation
 */
class ADA(context: Context, file:File): Operator(context, file) {

    var annotation:String? = null
    var mutant = Mutant(OperatorsEnum.ADA)

    override fun checkContext(): Boolean = true

    override fun mutate(): List<Mutant> {
        if (annotation == null) throw Exception("ADA with null annotation")

        val mutateVisitor = MutateVisitor(this)
        val compUnit = JavaParser.parse(file)
        val newCompUnit = compUnit.clone()

        mutateVisitor.visit(newCompUnit, null)
        mutant.compilationUnit = newCompUnit

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
        if (classOrInterfaceDeclaration != null)
            classOrInterfaceDeclaration.addAnnotation(AnnotationBuilder(annotation!!).build())
        else if (methodDeclaration != null)
            methodDeclaration.addAnnotation(AnnotationBuilder(annotation!!).build())
        else if (fieldDeclaration != null)
            fieldDeclaration.addAnnotation(AnnotationBuilder(annotation!!).build())
        else if (parameter != null)
            parameter.addAnnotation(AnnotationBuilder(annotation!!).build())
        else
            return false

        mutant.after = annotation!!
        locked = true

        return true
    }
}
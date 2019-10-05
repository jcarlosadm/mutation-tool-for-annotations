package mutation.tool.operator.chodr

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import com.google.common.collect.Collections2
import mutation.tool.annotation.builder.JavaAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.mutant.JavaMutateVisitor
import mutation.tool.operator.JavaOperator
import mutation.tool.operator.OperatorsEnum
import java.io.File

/**
 * Change order of annotations
 *
 * @param context context of this operator
 * @param file source file
 * @constructor create a CHODR operator
 */
class CHODR(context: Context, file: File) : JavaOperator(context, file) {
    override val mutateVisitor = JavaMutateVisitor(this)
    private val currentAnnotations = mutableListOf<AnnotationExpr>()
    private var javaMutant:JavaMutant? = null

    override fun checkContext(): Boolean {
        if (context.annotations.size > 1)
            return true
        return false
    }

    override fun mutate(): List<JavaMutant> {
        val mutants = mutableListOf<JavaMutant>()
        val annotations = context.annotations

        val originalSequence = (0 until annotations.size).toList()
        val permutations = Collections2.permutations((0..(annotations.size-1)).toMutableList())

        var originalDetected = false
        for (sequence in permutations) {
            if (!originalDetected && this.isOriginalSequence(sequence, originalSequence)) {
                originalDetected = true
            } else {
                currentAnnotations.clear()
                for (index in sequence) {
                    val builder = JavaAnnotationBuilder(annotations[index].string)
                    builder.build()
                    currentAnnotations += builder.annotationExpr!!
                }

                javaMutant = JavaMutant(OperatorsEnum.CHODR)
                javaMutant?.compilationUnit = this.visit()
                mutants += javaMutant!!
            }
        }

        return mutants
    }

    private fun isOriginalSequence(sequence: List<Int>, originalSequence: List<Int>): Boolean {
        for (index in (0 until sequence.size)) {
            if (sequence[index] != originalSequence[index])
                return false
        }

        return true
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            this.changeOrder(n, null, null, null)

    override fun visit(n: MethodDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            this.changeOrder(null, n, null, null)

    override fun visit(n: FieldDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            this.changeOrder(null, null, n, null)

    override fun visit(n: Parameter?, arg: Any?): Boolean = super.visit(n, arg) &&
            this.changeOrder(null, null, null, n)

    private fun changeOrder(
            classOrInterfaceDecl: ClassOrInterfaceDeclaration?,
            methodDeclaration: MethodDeclaration?,
            fieldDeclaration: FieldDeclaration?,
            parameter: Parameter?
    ): Boolean {
        when {
            classOrInterfaceDecl != null -> {
                classOrInterfaceDecl.annotations.clear()
                for (annotation in currentAnnotations) classOrInterfaceDecl.addAnnotation(annotation)
            }
            methodDeclaration != null -> {
                methodDeclaration.annotations.clear()
                for (annotation in currentAnnotations) methodDeclaration.addAnnotation(annotation)
            }
            fieldDeclaration != null -> {
                fieldDeclaration.annotations.clear()
                for (annotation in currentAnnotations) fieldDeclaration.addAnnotation(annotation)
            }
            parameter != null -> {
                parameter.annotations.clear()
                for (annotation in currentAnnotations) parameter.addAnnotation(annotation)
            }
            else -> return false
        }

        return true
    }

}
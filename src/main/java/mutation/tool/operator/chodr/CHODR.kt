package mutation.tool.operator.chodr

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import com.google.common.collect.Collections2
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import java.io.File

/**
 * Change order of annotations
 */
class CHODR(context: Context, file: File) : Operator(context, file) {
    private val currentAnnotations = mutableListOf<AnnotationExpr>()
    private var mutant:Mutant? = null

    override fun checkContext(): Boolean {
        if (context.getAnnotations().size > 1)
            return true
        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()
        val annotations = context.getAnnotations()

        val originalSequence = (0..(annotations.size-1)).toList()
        val permutations = Collections2.permutations((0..(annotations.size-1)).toMutableList())

        var originalDetected = false
        for (sequence in permutations) {
            if (!originalDetected && this.isOriginalSequence(sequence, originalSequence)) {
                originalDetected = true
            } else {
                currentAnnotations.clear()
                for (index in sequence) {
                    currentAnnotations += annotations[index]
                }

                mutant = Mutant(OperatorsEnum.CHODR)
                mutant?.compilationUnit = this.visit()
                mutants += mutant!!
            }
        }

        return mutants
    }

    private fun isOriginalSequence(sequence: List<Int>, originalSequence: List<Int>): Boolean {
        for (index in (0..(sequence.size - 1))) {
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
package mutation.tool.operator.chodr

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import com.google.common.collect.Collections2
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.getAnnotations
import java.io.File

/**
 * Change order of annotations
 */
class CHODR(context: Context, file: File) : Operator(context, file) {

    private val currentAnnotations = mutableListOf<AnnotationExpr>()
    private var mutant:Mutant? = null

    override fun checkContext(): Boolean {
        if (getAnnotations(context).size > 1)
            return true
        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        val mutateVisitor = MutateVisitor(this)
        val compUnit = JavaParser.parse(file)
        val annotations = getAnnotations(context)

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

                val newCompUnit = compUnit.clone()
                locked = false
                mutant = Mutant(OperatorsEnum.CHODR)
                mutateVisitor.visit(newCompUnit, null)
                mutant?.compilationUnit = newCompUnit
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
        if (classOrInterfaceDecl != null) {
            for (annotation in classOrInterfaceDecl.annotations) { mutant?.before = mutant?.before + annotation.toString() + "\n" }
            classOrInterfaceDecl.annotations.clear()
            for (annotation in currentAnnotations) classOrInterfaceDecl.addAnnotation(annotation)
        }
        else if (methodDeclaration != null) {
            for (annotation in methodDeclaration.annotations) { mutant?.before = mutant?.before + annotation.toString() + "\n" }
            methodDeclaration.annotations.clear()
            for (annotation in currentAnnotations) methodDeclaration.addAnnotation(annotation)
        }
        else if (fieldDeclaration != null) {
            for (annotation in fieldDeclaration.annotations) { mutant?.before = mutant?.before + annotation.toString() + "\n" }
            fieldDeclaration.annotations.clear()
            for (annotation in currentAnnotations) fieldDeclaration.addAnnotation(annotation)
        }
        else if (parameter != null) {
            for (annotation in parameter.annotations) { mutant?.before = mutant?.before + annotation.toString() + "\n" }
            parameter.annotations.clear()
            for (annotation in currentAnnotations) parameter.addAnnotation(annotation)
        } else
            return false

        for (annotation in currentAnnotations) { mutant?.after = mutant?.after + annotation.toString() + "\n" }
        locked = true

        return true
    }

}
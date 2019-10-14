package mutation.tool.operator.chodr

import com.google.common.collect.Collections2
import mutation.tool.annotation.builder.CSharpAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import mutation.tool.operator.OperatorsEnum
import org.w3c.dom.Node
import java.io.File

class CSharpCHODR(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    private val currentAnnotations = mutableListOf<Node>()
    private var mutant:CSharpMutant? = null

    override fun checkContext(): Boolean {
        if (context.annotations.size > 1)
            return true
        return false
    }

    override fun mutate(): List<CSharpMutant> {
        val mutants = mutableListOf<CSharpMutant>()
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
                    val builder = CSharpAnnotationBuilder(annotations[index].string)
                    builder.build()
                    currentAnnotations += builder.node!!
                }

                mutant = CSharpMutant(OperatorsEnum.CHODR)
                mutant?.rootNode = this.visit()
                mutants += mutant!!
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

    override fun visitClass(node: Node): Boolean = super.visitClass(node) && changeOrder(getAnnotations(node))

    override fun visitMethod(node: Node): Boolean = super.visitMethod(node) && changeOrder(getAnnotations(node))

    override fun visitParameter(node: Node): Boolean = super.visitParameter(node) && changeOrder(getAnnotations(node))

    override fun visitProperty(node: Node): Boolean = super.visitProperty(node) && changeOrder(getAnnotations(node))

    private fun changeOrder(annotations: List<Node>): Boolean {
        val parent = annotations.first().parentNode
        for (index in 0 until (annotations.size - 1)) {
            parent.removeChild(annotations[index])
        }

        val list = mutableListOf<Node>()
        for (annotation in currentAnnotations) {
            list.add(parent.ownerDocument.importNode(annotation, true))
        }

        parent.replaceChild(list.last(), annotations.last())
        for (index in (list.size - 2) downTo 0) {
            parent.insertBefore(list[index], list[index + 1])
        }

        return true
    }
}
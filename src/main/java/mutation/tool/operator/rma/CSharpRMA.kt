package mutation.tool.operator.rma

import mutation.tool.annotation.builder.CSharpAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import mutation.tool.operator.OperatorsEnum
import org.w3c.dom.Node
import java.io.File

class CSharpRMA(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)
    private var currentMutant:CSharpMutant? = null
    private var currentAnnotation: Node? = null

    override fun checkContext(): Boolean = (context.annotations).isNotEmpty()

    override fun mutate(): List<CSharpMutant> {
        val mutants = mutableListOf<CSharpMutant>()

        for (annotation in context.annotations) {
            val builder = CSharpAnnotationBuilder(annotation.string)
            builder.build()
            currentAnnotation = builder.node!!
            currentMutant = CSharpMutant(OperatorsEnum.RMA)
            currentMutant?.rootNode = this.visit()
            mutants.add(currentMutant!!)
        }

        return mutants
    }

    override fun visitClass(node: Node): Boolean = super.visitClass(node) &&
            removeCurrentAnnotation(getAnnotations(node, InsertionPoint.CLASS))

    override fun visitMethod(node: Node): Boolean = super.visitMethod(node) &&
            removeCurrentAnnotation(getAnnotations(node, InsertionPoint.METHOD))

    override fun visitParameter(node: Node): Boolean = super.visitParameter(node) &&
            removeCurrentAnnotation(getAnnotations(node, InsertionPoint.PARAMETER))

    override fun visitProperty(node: Node): Boolean = super.visitProperty(node) &&
            removeCurrentAnnotation(getAnnotations(node, InsertionPoint.PROPERTY))

    private fun removeCurrentAnnotation(annotations:List<Node>): Boolean {
        if (currentAnnotation == null)
            return false

        for (annotation in annotations) {
            if (annotation.textContent == currentAnnotation?.textContent) {
                annotation.parentNode.removeChild(annotation)
                return true
            }
        }

        return false
    }
}
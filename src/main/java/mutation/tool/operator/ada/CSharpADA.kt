package mutation.tool.operator.ada

import mutation.tool.annotation.builder.CSharpAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node
import java.io.File

class CSharpADA(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    var annotation:String? = null

    var mutant = CSharpMutant(OperatorsEnum.ADA)

    override fun checkContext(): Boolean {
        if (annotation == null) return false

        for (annotationContext in context.annotations) {
            if (getName(annotation!!) == annotationContext.name) return false
        }

        return true
    }

    private fun getName(annotation: String): String? {
        if (annotation.contains("("))
            return annotation.substring(0, annotation.indexOf("("))
        return annotation
    }

    override fun mutate(): List<CSharpMutant> {
        if (annotation == null) throw Exception("ADA with null annotation")
        mutant.rootNode = this.visit()
        return listOf(mutant)
    }

    override fun visitClass(node: Node): Boolean = super.visitClass(node) &&
            insertAnnotation(node, InsertionPoint.CLASS)

    override fun visitMethod(node: Node): Boolean = super.visitMethod(node) &&
            insertAnnotation(node, InsertionPoint.METHOD)

    override fun visitProperty(node: Node): Boolean = super.visitProperty(node) &&
            insertAnnotation(node, InsertionPoint.PROPERTY)

    override fun visitParameter(node: Node): Boolean = super.visitParameter(node) &&
            insertAnnotation(node, InsertionPoint.PARAMETER)

    private fun insertAnnotation(node:Node, insertionPoint:InsertionPoint):Boolean {
        val builder = CSharpAnnotationBuilder("[$annotation]\n")
        builder.build()
        val newNode = node.ownerDocument.importNode(builder.node, true)

        if (insertionPoint == InsertionPoint.PARAMETER || insertionPoint == InsertionPoint.PROPERTY) {
            val decl = getTagNode(node, "decl", false)!!
            if (decl.firstChild != null) {
                decl.insertBefore(newNode, decl.firstChild)
            } else
                decl.appendChild(newNode)
        } else {
            if (node.firstChild != null) {
                node.insertBefore(newNode, node.firstChild)
            } else
                node.appendChild(newNode)
        }

        return true
    }
}
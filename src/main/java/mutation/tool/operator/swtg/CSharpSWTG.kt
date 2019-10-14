package mutation.tool.operator.swtg

import mutation.tool.annotation.builder.CSharpAnnotationBuilder
import mutation.tool.annotation.finder.cSharpAnnotationFinder
import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.isSameClass
import mutation.tool.util.isSameMethod
import mutation.tool.util.isSameParameter
import mutation.tool.util.isSameProp
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node
import java.io.File

class CSharpSWTG(context: Context, file: File, private val allContexts: List<Context>) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    lateinit var mapContextType:Map<String, List<InsertionPoint>>

    private lateinit var currentMutant: CSharpMutant
    private lateinit var currentAnnotation: Node
    private lateinit var currentOtherContext: Context
    private var lockedInsert = false

    override fun checkContext(): Boolean {
        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            mapContextType.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }

            if (ok && mapContextType[validKey] != null) {
                for (insertionPoint in mapContextType.getValue(validKey)) {
                    if (context.getInsertionPoint() != insertionPoint)
                        return true
                }
            }
        }

        return false
    }

    override fun mutate(): List<CSharpMutant> {
        val mutants = mutableListOf<CSharpMutant>()

        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            mapContextType.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }

            if (!ok || mapContextType[validKey] == null) continue

            for (insertionPoint in mapContextType.getValue(validKey)) {
                if (context.getInsertionPoint() == insertionPoint) continue

                for (otherContext in allContexts) {
                    if (otherContext.getInsertionPoint() != insertionPoint) continue

                    val builder = CSharpAnnotationBuilder(annotation.string)
                    builder.build()

                    lockedInsert = false
                    currentMutant = CSharpMutant(OperatorsEnum.SWTG)
                    currentAnnotation = builder.node!!
                    currentOtherContext = otherContext

                    currentMutant.rootNode = this.visit()
                    mutants += currentMutant
                }
            }
        }

        return mutants
    }

    override fun visitClass(node: Node): Boolean {
        if (super.visitClass(node)) return removeAnnotation(getAnnotations(node))
        else if (!lockedInsert && isSameClass(currentOtherContext, node)) addAnnotation(node, InsertionPoint.CLASS)
        return false
    }

    override fun visitMethod(node: Node): Boolean {
        if (super.visitMethod(node)) return removeAnnotation(getAnnotations(node))
        else if (!lockedInsert && isSameMethod(currentOtherContext, node)) addAnnotation(node, InsertionPoint.METHOD)
        return false
    }

    override fun visitParameter(node: Node): Boolean {
        if (super.visitParameter(node)) return removeAnnotation(getAnnotations(node))
        else if (!lockedInsert && isSameParameter(currentOtherContext, node)) addAnnotation(node, InsertionPoint.PARAMETER)
        return false
    }

    override fun visitProperty(node: Node): Boolean {
        if (super.visitProperty(node)) return removeAnnotation(getAnnotations(node))
        else if (!lockedInsert && isSameProp(currentOtherContext, node)) addAnnotation(node, InsertionPoint.PROPERTY)
        return false
    }

    private fun removeAnnotation(annotations: List<Node>): Boolean {
        for (annotation in annotations) {
            if (annotation.textContent == currentAnnotation.textContent) {
                annotation.parentNode.removeChild(annotation)
                return true
            }
        }

        return false
    }

    private fun addAnnotation(node: Node, insertionPoint: InsertionPoint) {
        val newNode = node.ownerDocument.importNode(currentAnnotation, true)

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

        lockedInsert = true
    }
}
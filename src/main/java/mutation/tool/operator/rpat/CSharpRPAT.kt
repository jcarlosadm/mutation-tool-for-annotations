package mutation.tool.operator.rpat

import mutation.tool.annotation.builder.CSharpAnnotationBuilder
import mutation.tool.annotation.finder.cSharpAnnotationFinder
import mutation.tool.context.Context
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.Language
import mutation.tool.util.numOfAnnotationAttributes
import mutation.tool.util.xml.codeToDocument
import mutation.tool.util.xml.getAllTagNodes
import org.w3c.dom.Node
import java.io.File

class CSharpRPAT(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    lateinit var map: Map<String, Map<String, List<Map<String, String>>>>

    private lateinit var currentMutant:CSharpMutant
    private lateinit var currentAnnotation: Node
    private lateinit var currentAttr:String
    private lateinit var currentAttrRep:String
    private lateinit var currentAttrRepVal: String

    override fun checkContext(): Boolean {
        for (annotation in context.annotations){
            var ok = false
            var validKey = ""
            map.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok) continue

            if (numOfAnnotationAttributes(annotation.string) == 1 && map.getValue(validKey).containsKey(""))
                return true
            else if (numOfAnnotationAttributes(annotation.string) > 1){
                if (annotation.genericPair == null) return false
                for (pair in annotation.genericPair) {
                    // if present on map
                    if (!map.getValue(validKey).containsKey(pair.key)) continue

                    for (attrMap in map.getValue(validKey).getValue(pair.key)) {
                        var notContain = true

                        for (anotherPair in annotation.genericPair) {
                            if (anotherPair.key == attrMap.getValue("name")) {
                                notContain = false
                                break
                            }
                        }

                        if (notContain) return true
                    }
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
            map.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok) continue

            if (numOfAnnotationAttributes(annotation.string) == 1 && map.getValue(validKey).containsKey("")) {
                for (attrMap in map.getValue(validKey).getValue("")) {
                    val builder = CSharpAnnotationBuilder(annotation.string)
                    builder.build()
                    createMutant(builder.node!!, "", attrMap, mutants)
                }
            }
            else if (numOfAnnotationAttributes(annotation.string) > 1) {

                if (annotation.genericPair == null) continue
                for (pair in annotation.genericPair) {
                    if (!map.getValue(validKey).contains(pair.key)) continue

                    for (attrMap in map.getValue(validKey).getValue(pair.key)) {
                        var notContain = true

                        for (anotherPair in annotation.genericPair) {
                            if (anotherPair.key == attrMap.getValue("name")) {
                                notContain = false
                                break
                            }
                        }

                        if (notContain) {
                            val builder = CSharpAnnotationBuilder(annotation.string)
                            builder.build()
                            createMutant(builder.node!!, pair.key, attrMap, mutants)
                        }
                    }
                }
            }
        }

        return mutants
    }

    private fun createMutant(
            annotation: Node,
            attr: String,
            attrMap: Map<String, String>,
            mutants: MutableList<CSharpMutant>
    ) {
        currentAnnotation = annotation
        currentMutant = CSharpMutant(OperatorsEnum.RPAT)
        currentAttr = attr
        currentAttrRep = attrMap.getValue("name")
        currentAttrRepVal = attrMap.getValue("value")

        currentMutant.rootNode = this.visit()
        mutants += currentMutant
    }

    override fun visitClass(node: Node): Boolean = super.visitClass(node) && replacement(getAnnotations(node))

    override fun visitMethod(node: Node): Boolean = super.visitMethod(node) && replacement(getAnnotations(node))

    override fun visitParameter(node: Node): Boolean = super.visitParameter(node) && replacement(getAnnotations(node))

    override fun visitProperty(node: Node): Boolean = super.visitProperty(node) && replacement(getAnnotations(node))

    private fun replacement(annotations: List<Node>): Boolean {
        for (annotation in annotations) {
            val annotationAdapter = AnnotationAdapter(annotation)
            if (annotationAdapter.name != AnnotationAdapter(currentAnnotation).name) continue

            if (numOfAnnotationAttributes(annotation) > 1 && annotationAdapter.genericPair != null) {
                for (pair in annotationAdapter.genericPair) {
                    if (pair.key == currentAttr){
                        val arguments = getAllTagNodes(annotation, "argument", emptyList())
                        val argument = arguments.find { it.textContent.contains("=") &&
                                it.textContent.split("=").first().trim() == currentAttr } ?: return false

                        val newAttr = annotation.ownerDocument.importNode(codeToDocument(
                                "$currentAttrRep=$currentAttrRepVal", Language.C_SHARP).firstChild,true)

                        val parent = argument.parentNode
                        parent.replaceChild(newAttr, argument)

                        break
                    }
                }
            } else {
                val builder = CSharpAnnotationBuilder("[${annotationAdapter.name}(" +
                        "$currentAttrRep = $currentAttrRepVal)]")
                builder.build()

                val newAnnotation = annotation.ownerDocument.importNode(builder.node, true)
                val parent = annotation.parentNode

                parent.replaceChild(newAnnotation, annotation)
            }

            return true
        }

        return false
    }
}
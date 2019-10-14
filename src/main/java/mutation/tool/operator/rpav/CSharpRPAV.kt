package mutation.tool.operator.rpav

import mutation.tool.annotation.builder.CSharpAnnotationBuilder
import mutation.tool.annotation.finder.cSharpAnnotationFinder
import mutation.tool.context.Context
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.numOfAnnotationAttributes
import org.w3c.dom.Node
import java.io.File

class CSharpRPAV(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    lateinit var map:Map<String, Map<String, List<String>>>

    private lateinit var currentAnnotation: Node
    private lateinit var currentAttr: String
    private lateinit var currentAttrValue: String
    private lateinit var currentMutant: CSharpMutant

    override fun checkContext(): Boolean {
        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok || numOfAnnotationAttributes(annotation.string) == 0) continue

            val builder = CSharpAnnotationBuilder(annotation.string)
            builder.build()
            if ((numOfAnnotationAttributes(annotation.string) == 1 && map.getValue(validKey).containsKey("") &&
                            checkSingleAnnotation(builder.node!!, validKey)) ||
                    (numOfAnnotationAttributes(annotation.string) > 1 &&
                            checkNormalAnnotation(builder.node!!, validKey)))
                return true
        }

        return false
    }

    private fun checkNormalAnnotation(annotation: Node, validKey:String): Boolean {
        val annotationAdapter = AnnotationAdapter(annotation)
        if (annotationAdapter.genericPair == null) return false

        for (pair in annotationAdapter.genericPair) {
            if (map.getValue(validKey).containsKey(pair.key))
                for(value in map.getValue(validKey).getValue(pair.key))
                    if (value != pair.value) return true
        }
        return false
    }

    private fun checkSingleAnnotation(annotation: Node, validKey: String): Boolean {
        val annotationAdapter = AnnotationAdapter(annotation)
        if (annotationAdapter.genericPair != null && annotationAdapter.genericPair.isNotEmpty()) return false
        var value = annotation.textContent
        value = value.substring(value.indexOf("(") + 1, value.indexOf(")")).trim()

        for (mapValue in map.getValue(validKey).getValue(""))
            if (value != mapValue) return true

        return false
    }

    override fun mutate(): List<CSharpMutant> {
        val mutants = mutableListOf<CSharpMutant>()

        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }

            if (!ok || numOfAnnotationAttributes(annotation.string) == 0) continue

            val builder = CSharpAnnotationBuilder(annotation.string)
            builder.build()
            val node = builder.node!!
            if (numOfAnnotationAttributes(annotation.string) == 1 && map.getValue(validKey).containsKey("") &&
                    this.checkSingleAnnotation(builder.node!!, validKey)) {
                var value = node.textContent
                value = value.substring(value.indexOf("(") + 1, value.indexOf(")")).trim()

                for (attrValue in map.getValue(validKey).getValue("")) {

                    if (attrValue.trim() != value)
                        genMutant(node, "", attrValue, mutants)
                }

            }
            else if (numOfAnnotationAttributes(annotation.string) > 1) {
                if (annotation.genericPair == null) continue
                for (pair in annotation.genericPair) {
                    if (map.getValue(validKey).containsKey(pair.key)) {
                        for (attrValue in map.getValue(validKey).getValue(pair.key)) {
                            if (attrValue != pair.value)
                                genMutant(node, pair.key, attrValue, mutants)
                        }
                    }
                }
            }
        }


        return mutants
    }

    private fun genMutant(
            annotation: Node,
            attr: String,
            attrValue: String,
            mutants: MutableList<CSharpMutant>
    ) {
        currentAnnotation = annotation
        currentAttr = attr
        currentAttrValue = attrValue
        currentMutant = CSharpMutant(OperatorsEnum.RPAV)

        currentMutant.rootNode = this.visit()
        mutants += currentMutant
    }

    override fun visitClass(node: Node): Boolean = super.visitClass(node) && changeValue(getAnnotations(node))

    override fun visitMethod(node: Node): Boolean = super.visitMethod(node) && changeValue(getAnnotations(node))

    override fun visitParameter(node: Node): Boolean = super.visitParameter(node) && changeValue(getAnnotations(node))

    override fun visitProperty(node: Node): Boolean = super.visitProperty(node) && changeValue(getAnnotations(node))

    private fun changeValue(annotations: List<Node>): Boolean {
        for (annotation in annotations) {
            val annotationAdapter = AnnotationAdapter(annotation)
            if (annotationAdapter.name == AnnotationAdapter(currentAnnotation).name) {
                if (numOfAnnotationAttributes(annotation.textContent) == 1) {
                    val builder = CSharpAnnotationBuilder("[${annotationAdapter.name}" +
                            "($currentAttrValue)]")
                    builder.build()
                    val newNode = annotation.ownerDocument.importNode(builder.node, true)

                    val parent = annotation.parentNode
                    parent.replaceChild(newNode, annotation)
                }
                else {
                    var string = "[${annotationAdapter.name}("
                    val pairs = annotationAdapter.genericPair ?: continue
                    for (pair in pairs) {
                        string += if (pair.key == currentAttr) {
                            "$currentAttr = $currentAttrValue"
                        } else {
                            "${pair.key} = ${pair.value}"
                        }

                        string += ","
                    }
                    string = string.removeSuffix(",") + ")]"

                    val builder = CSharpAnnotationBuilder(string)
                    builder.build()
                    val newNode = annotation.ownerDocument.importNode(builder.node, true)

                    val parent = annotation.parentNode
                    parent.replaceChild(newNode, annotation)
                }

                return true
            }
        }

        return false
    }
}
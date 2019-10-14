package mutation.tool.operator.adat

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

class CSharpADAT(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    lateinit var map:Map<String, List<Map<String, String>>>

    private lateinit var currentMutant: CSharpMutant
    private lateinit var currentAnnotation:String
    private lateinit var currentAttr:Map<String, String>

    override fun checkContext(): Boolean {
        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok) continue

            if (numOfAnnotationAttributes(annotation.string) > 1) {

                for (attr in map.getValue(validKey)) {
                    var notEqual = true
                    if (annotation.genericPair == null) continue
                    for (pair in annotation.genericPair) {
                        if (attr.getValue("name") == pair.key) {
                            notEqual = false
                            break
                        }
                    }

                    if (notEqual) return true
                }
            } else if(numOfAnnotationAttributes(annotation.string) == 1) {
                for (attr in map.getValue(validKey)) {
                    if (attr.containsKey("asSingle") && attr.getValue("asSingle") == "true") return true
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

            if (numOfAnnotationAttributes(annotation.string) > 1) {

                for (attr in map.getValue(validKey)) {
                    var notEqual = true
                    if (annotation.genericPair == null) continue
                    for (pair in annotation.genericPair) {
                        if (attr.getValue("name") == pair.key) {
                            notEqual = false
                            break
                        }
                    }

                    if (notEqual) createMutant(annotation, attr, mutants)
                }
            } else if(numOfAnnotationAttributes(annotation.string) == 1) {
                var containsSingle = false
                for (attr in map.getValue(validKey)) {
                    if (attr.containsKey("asSingle") && attr.getValue("asSingle") == "true") {
                        containsSingle = true
                        break
                    }
                }

                if (containsSingle) {
                    for (attr in map.getValue(validKey)) {
                        if (!attr.containsKey("asSingle"))
                            createMutant(annotation, attr, mutants)
                    }
                }
            }
        }

        return mutants
    }

    private fun createMutant(
            annotation: AnnotationAdapter,
            attr: Map<String, String>,
            mutants: MutableList<CSharpMutant>
    ) {
        currentMutant = CSharpMutant(OperatorsEnum.ADAT)
        currentAnnotation = annotation.name
        currentAttr = attr

        currentMutant.rootNode = this.visit()
        mutants += currentMutant
    }

    override fun visitClass(node: Node): Boolean = super.visitClass(node) && addAttr(getAnnotations(node))

    override fun visitMethod(node: Node): Boolean = super.visitMethod(node) && addAttr(getAnnotations(node))

    override fun visitParameter(node: Node): Boolean = super.visitParameter(node) && addAttr(getAnnotations(node))

    override fun visitProperty(node: Node): Boolean = super.visitProperty(node) && addAttr(getAnnotations(node))

    private fun addAttr(annotations: List<Node>): Boolean {
        for (annotation in annotations) {
            val annotationAdapter = AnnotationAdapter(annotation)
            if (annotationAdapter.name == currentAnnotation) {
                val lastArgument = getAllTagNodes(annotation, "argument", emptyList()).last()
                val parent = lastArgument.parentNode
                val comma =  parent.ownerDocument.createTextNode(",")

                val code = currentAttr.getValue("name") + "=" + currentAttr.getValue("value")
                val newNode = parent.ownerDocument.importNode(codeToDocument(code, Language.C_SHARP).firstChild,
                        true)
                parent.replaceChild(newNode, lastArgument)
                parent.insertBefore(comma, newNode)
                parent.insertBefore(lastArgument, comma)

                return true
            }
        }

        return false
    }
}
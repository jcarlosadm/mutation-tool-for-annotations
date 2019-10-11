package mutation.tool.operator.rmat

import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.builder.CSharpAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.CSharpOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.numOfAnnotationAttributes
import org.w3c.dom.Node
import java.io.File

class CSharpRMAT(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    private var currentMutant: CSharpMutant? = null
    private var currentAnnotation: AnnotationAdapter? = null
    private var currentIndex:Int? = null

    override fun checkContext(): Boolean {
        for (annotation in context.annotations) {
            if (annotation.string.contains(Regex("\\((.*?)\\)"))) {
                return true
            }
        }

        return false
    }

    override fun mutate(): List<CSharpMutant> {
        val mutants = mutableListOf<CSharpMutant>()

        for (annotation in context.annotations) {
            val builder = CSharpAnnotationBuilder(annotation.string)
            builder.build()
            val nAttr = numOfAnnotationAttributes(builder.node!!)
            for (index in 0 until nAttr) {
                currentAnnotation = annotation
                currentIndex = index
                currentMutant = CSharpMutant(OperatorsEnum.RMAT)
                currentMutant!!.rootNode = this.visit()
                mutants.add(currentMutant!!)
            }
        }

        return mutants
    }

    override fun visitClass(node: Node): Boolean = super.visitClass(node) && removeAttribute(getAnnotations(node))

    override fun visitMethod(node: Node): Boolean = super.visitMethod(node) && removeAttribute(getAnnotations(node))

    override fun visitParameter(node: Node): Boolean = super.visitParameter(node) && removeAttribute(getAnnotations(node))

    override fun visitProperty(node: Node): Boolean = super.visitProperty(node) && removeAttribute(getAnnotations(node))

    private fun removeAttribute(annotations:List<Node>):Boolean {
        for (annotation in annotations) {
            if (annotation.textContent == currentAnnotation!!.string) {
                val stringRep = annotation.textContent
                val nAttr = numOfAnnotationAttributes(annotation)
                var annotationString = ""

                when (nAttr) {
                    0 -> return false
                    1 -> {
                        val substring1 = stringRep.substring(0, stringRep.indexOf("("))
                        val substring2 = stringRep.substring(stringRep.indexOf(")", substring1.length) + 1)
                        annotationString = substring1 + substring2
                    }
                    else -> {
                        val substring1 = stringRep.substring(0, stringRep.indexOf("(") + 1)
                        val substring2 = stringRep.substring(stringRep.lastIndexOf(")"))
                        var middle = stringRep.removePrefix(substring1).removeSuffix(substring2)
                        middle = middle.replace(" ", "")

                        val stringArray = middle.split(",").toMutableList()
                        stringArray.removeAt(currentIndex!!)

                        middle = stringArray.joinToString(",")
                        annotationString = substring1 + middle + substring2
                    }
                }

                val builder = CSharpAnnotationBuilder(annotationString)
                builder.build()
                builder.node!!.parentNode.removeChild(builder.node)

                val parent = annotation.parentNode
//                parent.insertBefore(builder.node?.cloneNode(false), annotation)
//                parent.removeChild(annotation)
                parent.replaceChild(builder.node, annotation)
                return true
            }
        }

        return false
    }
}
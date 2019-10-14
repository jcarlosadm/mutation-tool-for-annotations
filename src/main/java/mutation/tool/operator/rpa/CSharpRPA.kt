package mutation.tool.operator.rpa

import mutation.tool.annotation.builder.CSharpAnnotationBuilder
import mutation.tool.annotation.finder.cSharpAnnotationFinder
import mutation.tool.context.Context
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import mutation.tool.operator.OperatorsEnum
import org.w3c.dom.Node
import java.io.File

class CSharpRPA(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    lateinit var switchMap:Map<String, List<String>>

    private lateinit var currentCSharpMutant:CSharpMutant
    private lateinit var currentAnnotation: Node
    private lateinit var currentAnnotationRep: String

    override fun checkContext(): Boolean {
        for (annotation in context.annotations){
            var ok = false
            var validKey = ""
            switchMap.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (ok) {
                for (annotation2 in (context.annotations - annotation)) {
                    ok = true
                    switchMap.getValue(validKey).forEach { if (cSharpAnnotationFinder(annotation2, it)) ok = false }
                    if (!ok) return false
                }
                return true
            }
        }

        return false
    }

    override fun mutate(): List<CSharpMutant> {
        val mutants = mutableListOf<CSharpMutant>()

        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            switchMap.keys.forEach { if (cSharpAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok || switchMap[validKey] == null) continue

            ok = true
            for (annotation2 in (context.annotations - annotation)) {
                switchMap.getValue(validKey).forEach { if (cSharpAnnotationFinder(annotation2, it)) ok = false }
            }
            if (!ok) continue

            val builder = CSharpAnnotationBuilder(annotation.string)
            builder.build()
            currentAnnotation = builder.node!!

            for (annotationRep in switchMap.getValue(validKey)){
                currentAnnotationRep = annotationRep
                currentCSharpMutant = CSharpMutant(OperatorsEnum.RPA)

                currentCSharpMutant.rootNode = this.visit()
                mutants += currentCSharpMutant
            }
        }

        return mutants
    }

    override fun visitClass(node: Node): Boolean = super.visitClass(node) && replaceAnnotation(getAnnotations(node))

    override fun visitMethod(node: Node): Boolean = super.visitMethod(node) && replaceAnnotation(getAnnotations(node))

    override fun visitParameter(node: Node): Boolean = super.visitParameter(node) && replaceAnnotation(getAnnotations(node))

    override fun visitProperty(node: Node): Boolean = super.visitProperty(node) && replaceAnnotation(getAnnotations(node))

    private fun replaceAnnotation(annotations: List<Node>):Boolean {
        for (annotation in annotations) {
            if (annotation.textContent == currentAnnotation.textContent) {
                val builder = CSharpAnnotationBuilder("[$currentAnnotationRep]")
                builder.build()

                val parent = annotation.parentNode
                val newNode = parent.ownerDocument.importNode(builder.node, true)
                parent.replaceChild(newNode, annotation)

                return true
            }
        }

        return false
    }
}
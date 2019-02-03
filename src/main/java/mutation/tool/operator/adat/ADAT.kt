package mutation.tool.operator.adat

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import mutation.tool.annotation.AnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import java.io.File

/**
 * Add valid attribute to annotation
 */
class ADAT(context: Context, file:File) : Operator(context, file) {
    lateinit var map:Map<String, List<Map<String, String>>>

    private lateinit var currentMutant:Mutant
    private lateinit var currentAnnotation:AnnotationExpr
    private lateinit var currentAttr:Map<String, String>

    override fun checkContext(): Boolean {
        for (annotation in context.getAnnotations()) {
            if (!map.containsKey(annotation.nameAsString)) continue

            if (annotation.isNormalAnnotationExpr) {
                annotation as NormalAnnotationExpr

                for (attr in map.getValue(annotation.nameAsString)) {
                    var notEqual = true
                    for (pair in annotation.pairs) {
                        if (attr.getValue("name") == pair.nameAsString) {
                            notEqual = false
                            break
                        }
                    }

                    if (notEqual) return true
                }
            } else if(annotation.isSingleMemberAnnotationExpr) {
                for (attr in map.getValue(annotation.nameAsString)) {
                    if (attr.containsKey("asSingle") && attr.getValue("asSingle") == "true") return true
                }
            }
        }

        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        for (annotation in context.getAnnotations()) {
            if (!map.containsKey(annotation.nameAsString)) continue

            if (annotation.isNormalAnnotationExpr) {
                annotation as NormalAnnotationExpr

                for (attr in map.getValue(annotation.nameAsString)) {
                    var notEqual = true
                    for (pair in annotation.pairs) {
                        if (attr.getValue("name") == pair.nameAsString) {
                            notEqual = false
                            break
                        }
                    }

                    if (notEqual) createMutant(annotation, attr, mutants)
                }
            } else if(annotation.isSingleMemberAnnotationExpr) {
                var containsSingle = false
                for (attr in map.getValue(annotation.nameAsString)) {
                    if (attr.containsKey("asSingle") && attr.getValue("asSingle") == "true") {
                        containsSingle = true
                        break
                    }
                }

                if (containsSingle) {
                    for (attr in map.getValue(annotation.nameAsString)) {
                        if (!attr.containsKey("asSingle"))
                            createMutant(annotation, attr, mutants)
                    }
                }
            }
        }

        return mutants
    }

    private fun createMutant(
            annotation: AnnotationExpr,
            attr: Map<String, String>,
            mutants: MutableList<Mutant>
    ) {
        currentMutant = Mutant(OperatorsEnum.ADAT)
        currentAnnotation = annotation
        currentAttr = attr

        currentMutant.compilationUnit = this.visit()
        mutants += currentMutant
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            addAttr(n!!.annotations)

    override fun visit(n: MethodDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            addAttr(n!!.annotations)

    override fun visit(n: FieldDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            addAttr(n!!.annotations)

    override fun visit(n: Parameter?, arg: Any?): Boolean = super.visit(n, arg) &&
            addAttr(n!!.annotations)

    private fun addAttr(annotations: NodeList<AnnotationExpr>): Boolean {
        for (annotation in annotations) {
            if (annotation.nameAsString == currentAnnotation.nameAsString) {
                if (annotation.isNormalAnnotationExpr) {
                    annotation as NormalAnnotationExpr

                    annotation.addPair(currentAttr.getValue("name"), currentAttr.getValue("value"))
                    return true
                } else {
                    for (attr in map.getValue(annotation.nameAsString)) {
                        if (attr.containsKey("asSingle")) {
                            val defaultValue = getValue(annotation.toString())
                            val annotationString = makeString(annotation.nameAsString, attr, defaultValue)
                            val otherAnnotation = AnnotationBuilder(annotationString).build()

                            otherAnnotation as NormalAnnotationExpr
                            otherAnnotation.addPair(currentAttr.getValue("name"), currentAttr.getValue("value"))
                            annotation.replace(otherAnnotation)

                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    private fun getValue(annotationString: String): String = annotationString.substring(annotationString.
            indexOf("(") + 1, annotationString.indexOf(")")).trim()

    private fun makeString(nameAsString: String, attr: Map<String, String>, defaultValue: String): String =
            nameAsString + "(" + attr.getValue("name") + " = " + defaultValue + ")"
}
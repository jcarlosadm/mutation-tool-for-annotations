package mutation.tool.operator.rpat

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
 * Replace a code annotation attribute by another
 */
class RPAT(context: Context, file: File) : Operator(context, file) {
    lateinit var map: Map<String, Map<String, List<Map<String, String>>>>

    private lateinit var currentMutant:Mutant
    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentAttr:String
    private lateinit var currentAttrRep:String
    private lateinit var currentAttrRepVal: String

    override fun checkContext(): Boolean {
        for (annotation in context.getAnnotations()){
            if (!map.containsKey(annotation.nameAsString)) continue

            if (annotation.isSingleMemberAnnotationExpr && map.getValue(annotation.nameAsString).containsKey(""))
                return true
            else if (annotation.isNormalAnnotationExpr){
                annotation as NormalAnnotationExpr
                // check each attr of annotation
                for (pair in annotation.pairs) {
                    // if present on map
                    if (!map.getValue(annotation.nameAsString).containsKey(pair.nameAsString)) continue

                    for (attrMap in map.getValue(annotation.nameAsString).getValue(pair.nameAsString)) {
                        var notContain = true

                        for (anotherPair in annotation.pairs) {
                            if (anotherPair.nameAsString == attrMap.getValue("name")) {
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

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        for (annotation in context.getAnnotations()) {
            if (!map.containsKey(annotation.nameAsString)) continue

            if (annotation.isSingleMemberAnnotationExpr && map.getValue(annotation.nameAsString).containsKey("")) {
                for (attrMap in map.getValue(annotation.nameAsString).getValue(""))
                    createMutant(annotation, "", attrMap, mutants)
            }
            else if (annotation.isNormalAnnotationExpr) {
                annotation as NormalAnnotationExpr

                for (pair in annotation.pairs) {
                    if (!map.getValue(annotation.nameAsString).contains(pair.nameAsString)) continue

                    for (attrMap in map.getValue(annotation.nameAsString).getValue(pair.nameAsString)) {
                        var notContain = true

                        for (anotherPair in annotation.pairs) {
                            if (anotherPair.nameAsString == attrMap.getValue("name")) {
                                notContain = false
                                break
                            }
                        }

                        if (notContain)
                            createMutant(annotation, pair.nameAsString, attrMap, mutants)
                    }
                }
            }
        }

        return mutants
    }

    private fun createMutant(
            annotation: AnnotationExpr,
            attr: String,
            attrMap: Map<String, String>,
            mutants: MutableList<Mutant>
    ) {
        currentAnnotation = annotation
        currentMutant = Mutant(OperatorsEnum.RPAT)
        currentAttr = attr
        currentAttrRep = attrMap.getValue("name")
        currentAttrRepVal = attrMap.getValue("value")

        currentMutant.compilationUnit = this.visit()
        mutants += currentMutant
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            this.replacement(n!!.annotations)

    override fun visit(n: MethodDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            this.replacement(n!!.annotations)

    override fun visit(n: FieldDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            this.replacement(n!!.annotations)

    override fun visit(n: Parameter?, arg: Any?): Boolean = super.visit(n, arg) &&
            this.replacement(n!!.annotations)

    private fun replacement(annotations: NodeList<AnnotationExpr>): Boolean {
        for (annotation in annotations) {
            if (annotation.nameAsString != currentAnnotation.nameAsString) continue

            if (annotation.isNormalAnnotationExpr) {
                annotation as NormalAnnotationExpr

                for (pair in annotation.pairs) {
                    if (pair.nameAsString == currentAttr){
                        pair.remove()
                        break
                    }
                }

                annotation.addPair(currentAttrRep, currentAttrRepVal)
            } else {
                annotation.replace(AnnotationBuilder("@${annotation.nameAsString}(" +
                        "$currentAttrRep = $currentAttrRepVal)").build())
            }

            return true
        }

        return false
    }
}
package mutation.tool.operator.rpat

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import mutation.tool.annotation.builder.JavaAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.JavaOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.annotation.finder.javaAnnotationFinder
import java.io.File

/**
 * Replace a code annotation attribute by another
 *
 * @param context context of the operator
 * @param file source file
 * @constructor Create a RPAT operator instance
 */
class RPAT(context: Context, file: File) : JavaOperator(context, file) {

    /**
     * map that will help the RPAT operator to build the mutants
     * 
     * Structure of map:
     * annotation -> map (attribute -> list ( 
     *                                   map (attributeName -> attributeValue)
     *                                      ))
     */
    lateinit var map: Map<String, Map<String, List<Map<String, String>>>>

    private lateinit var currentJavaMutant:JavaMutant
    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentAttr:String
    private lateinit var currentAttrRep:String
    private lateinit var currentAttrRepVal: String

    override fun checkContext(): Boolean {
        for (annotation in context.getAnnotations()){
            var ok = false
            var validKey = ""
            map.keys.forEach { if (javaAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok) continue

            if (annotation.isSingleMemberAnnotationExpr && map.getValue(validKey).containsKey(""))
                return true
            else if (annotation.isNormalAnnotationExpr){
                annotation as NormalAnnotationExpr
                // check each attr of annotation
                for (pair in annotation.pairs) {
                    // if present on map
                    if (!map.getValue(validKey).containsKey(pair.nameAsString)) continue

                    for (attrMap in map.getValue(validKey).getValue(pair.nameAsString)) {
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

    override fun mutate(): List<JavaMutant> {
        val mutants = mutableListOf<JavaMutant>()

        for (annotation in context.getAnnotations()) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (javaAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok) continue

            if (annotation.isSingleMemberAnnotationExpr && map.getValue(validKey).containsKey("")) {
                for (attrMap in map.getValue(validKey).getValue(""))
                    createMutant(annotation, "", attrMap, mutants)
            }
            else if (annotation.isNormalAnnotationExpr) {
                annotation as NormalAnnotationExpr

                for (pair in annotation.pairs) {
                    if (!map.getValue(validKey).contains(pair.nameAsString)) continue

                    for (attrMap in map.getValue(validKey).getValue(pair.nameAsString)) {
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
            javaMutants: MutableList<JavaMutant>
    ) {
        currentAnnotation = annotation
        currentJavaMutant = JavaMutant(OperatorsEnum.RPAT)
        currentAttr = attr
        currentAttrRep = attrMap.getValue("name")
        currentAttrRepVal = attrMap.getValue("value")

        currentJavaMutant.compilationUnit = this.visit()
        javaMutants += currentJavaMutant
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
                annotation.replace(JavaAnnotationBuilder("@${annotation.nameAsString}(" +
                        "$currentAttrRep = $currentAttrRepVal)").build())
            }

            return true
        }

        return false
    }
}
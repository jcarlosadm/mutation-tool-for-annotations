package mutation.tool.operator.rpav

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import mutation.tool.annotation.AnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.annotationFinder
import java.io.File

/**
 * Replace a code annotation attribute value by another
 */
class RPAV(context: Context, file: File) : Operator(context, file) {
    lateinit var map:Map<String, Map<String, List<String>>>

    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentAttr: String
    private lateinit var currentAttrValue: String
    private lateinit var currentMutant: Mutant

    override fun checkContext(): Boolean {
        for (annotation in context.getAnnotations()) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (annotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok || annotation.isMarkerAnnotationExpr) continue

            if ((annotation.isSingleMemberAnnotationExpr && map.getValue(validKey).containsKey("") &&
                            checkSingleAnnotation(annotation, validKey)) ||
                    (annotation.isNormalAnnotationExpr && checkNormalAnnotation(annotation, validKey)))
                return true
        }

        return false
    }

    private fun checkNormalAnnotation(annotation: AnnotationExpr, validKey:String): Boolean {
        for (pair in (annotation as NormalAnnotationExpr).pairs) {
            if (map.getValue(validKey).containsKey(pair.nameAsString))
                for(value in map.getValue(validKey).getValue(pair.nameAsString))
                    if (value != pair.value.toString()) return true
        }
        return false
    }

    private fun checkSingleAnnotation(annotation: AnnotationExpr, validKey: String): Boolean {
        annotation as SingleMemberAnnotationExpr
        val value = annotation.memberValue.toString()

        for (mapValue in map.getValue(validKey).getValue(""))
            if (value != mapValue) return true

        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        for (annotation in context.getAnnotations()) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (annotationFinder(annotation, it)) {ok = true; validKey = it} }

            if (!ok || annotation.isMarkerAnnotationExpr) continue

            if (annotation.isSingleMemberAnnotationExpr && map.getValue(validKey).containsKey("") &&
                    this.checkSingleAnnotation(annotation, validKey)) {

                for (attrValue in map.getValue(validKey).getValue("")) {
                    if (attrValue != (annotation as SingleMemberAnnotationExpr).memberValue.toString())
                        genMutant(annotation, "", attrValue, mutants)
                }

            }
            else if (annotation.isNormalAnnotationExpr) {
                for (pair in (annotation as NormalAnnotationExpr).pairs) {
                    if (map.getValue(validKey).containsKey(pair.nameAsString)) {
                        for (attrValue in map.getValue(validKey).getValue(pair.nameAsString)) {
                            if (attrValue != pair.value.toString())
                                genMutant(annotation, pair.nameAsString, attrValue, mutants)
                        }
                    }
                }
            }
        }


        return mutants
    }

    private fun genMutant(
            annotation: AnnotationExpr,
            attr: String,
            attrValue: String,
            mutants: MutableList<Mutant>
    ) {
        currentAnnotation = annotation
        currentAttr = attr
        currentAttrValue = attrValue
        currentMutant = Mutant(OperatorsEnum.RPAV)

        currentMutant.compilationUnit = this.visit()
        mutants += currentMutant
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            changeValue(n!!.annotations)

    override fun visit(n: MethodDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            changeValue(n!!.annotations)

    override fun visit(n: FieldDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            changeValue(n!!.annotations)

    override fun visit(n: Parameter?, arg: Any?): Boolean = super.visit(n, arg) &&
            changeValue(n!!.annotations)

    private fun changeValue(annotations: NodeList<AnnotationExpr>): Boolean {
        for (annotation in annotations) {
            if (annotation.nameAsString == currentAnnotation.nameAsString) {
                if (annotation.isSingleMemberAnnotationExpr) {
                    annotation.replace(AnnotationBuilder("@${annotation.nameAsString}" +
                            "($currentAttrValue)").build())
                }
                else {
                    annotation as NormalAnnotationExpr
                    var string = "@${annotation.nameAsString}("
                    val pairs = annotation.pairs
                    for (i in 0..(pairs.size - 1)) {
                        val pair = pairs[i]
                        string += if (pair.nameAsString == currentAttr) {
                            "$currentAttr = $currentAttrValue"
                        } else {
                            "${pair.nameAsString} = ${pair.value}"
                        }

                        if (i < (pairs.size - 1))
                            string += ", "
                    }
                    string += ")"

                    annotation.replace(AnnotationBuilder(string).build())
                }

                return true
            }
        }

        return false
    }
}
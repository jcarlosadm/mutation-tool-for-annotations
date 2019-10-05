package mutation.tool.operator.rpav

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import mutation.tool.annotation.AnnotationType
import mutation.tool.annotation.builder.JavaAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.JavaOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.annotation.finder.javaAnnotationFinder
import mutation.tool.mutant.JavaMutateVisitor
import java.io.File

/**
 * Replace a code annotation attribute value by another
 * 
 * @param context context of the operator
 * @param file source file
 * @constructor Create a RPAV operator instance
 */
class RPAV(context: Context, file: File) : JavaOperator(context, file) {
    override val mutateVisitor = JavaMutateVisitor(this)

    /**
     * map that will help the RPAV operator to build the mutants
     * 
     * Structure of map:
     * annotation -> map (attributeName -> list ( attributeValue ) )
     */
    lateinit var map:Map<String, Map<String, List<String>>>

    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentAttr: String
    private lateinit var currentAttrValue: String
    private lateinit var currentJavaMutant: JavaMutant

    override fun checkContext(): Boolean {
        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (javaAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok || annotation.annotationType?.equals(AnnotationType.MARKER)!!) continue

            val builder = JavaAnnotationBuilder(annotation.string)
            builder.build()
            if ((annotation.annotationType?.equals(AnnotationType.SINGLE)!! && map.getValue(validKey).containsKey("") &&
                            checkSingleAnnotation(builder.annotationExpr!!, validKey)) ||
                    (annotation.annotationType?.equals(AnnotationType.NORMAL)!! &&
                            checkNormalAnnotation(builder.annotationExpr!!, validKey)))
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

    override fun mutate(): List<JavaMutant> {
        val mutants = mutableListOf<JavaMutant>()

        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (javaAnnotationFinder(annotation, it)) {ok = true; validKey = it} }

            if (!ok || annotation.annotationType?.equals(AnnotationType.MARKER)!!) continue

            val builder = JavaAnnotationBuilder(annotation.string)
            builder.build()
            val annotationExpr = builder.annotationExpr!!
            if (annotation.annotationType?.equals(AnnotationType.SINGLE)!! && map.getValue(validKey).containsKey("") &&
                    this.checkSingleAnnotation(builder.annotationExpr!!, validKey)) {

                for (attrValue in map.getValue(validKey).getValue("")) {
                    if (attrValue != (annotationExpr as SingleMemberAnnotationExpr).memberValue.toString())
                        genMutant(annotationExpr, "", attrValue, mutants)
                }

            }
            else if (annotationExpr.isNormalAnnotationExpr) {
                for (pair in (annotationExpr as NormalAnnotationExpr).pairs) {
                    if (map.getValue(validKey).containsKey(pair.nameAsString)) {
                        for (attrValue in map.getValue(validKey).getValue(pair.nameAsString)) {
                            if (attrValue != pair.value.toString())
                                genMutant(annotationExpr, pair.nameAsString, attrValue, mutants)
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
            javaMutants: MutableList<JavaMutant>
    ) {
        currentAnnotation = annotation
        currentAttr = attr
        currentAttrValue = attrValue
        currentJavaMutant = JavaMutant(OperatorsEnum.RPAV)

        currentJavaMutant.compilationUnit = this.visit()
        javaMutants += currentJavaMutant
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
                    val builder = JavaAnnotationBuilder("@${annotation.nameAsString}" +
                            "($currentAttrValue)")
                    builder.build()
                    annotation.replace(builder.annotationExpr!!)
                }
                else {
                    annotation as NormalAnnotationExpr
                    var string = "@${annotation.nameAsString}("
                    val pairs = annotation.pairs
                    for (i in 0 until pairs.size) {
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

                    val builder = JavaAnnotationBuilder(string)
                    builder.build()

                    annotation.replace(builder.annotationExpr!!)
                }

                return true
            }
        }

        return false
    }
}
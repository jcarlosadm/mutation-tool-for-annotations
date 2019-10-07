package mutation.tool.context.adapter

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.expr.*
import mutation.tool.annotation.AnnotationType
import mutation.tool.util.xml.getAllTagNodes
import mutation.tool.util.xml.getRootNode
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node

class AnnotationAdapter {

    val name:String
    val annotationType:AnnotationType?
    val imports = mutableListOf<String>()
    val pairs: NodeList<MemberValuePair>?
    val string: String

    constructor(annotationExpr: AnnotationExpr) {
        this.name = annotationExpr.nameAsString

        this.annotationType = when(annotationExpr) {
            is NormalAnnotationExpr -> AnnotationType.NORMAL
            is SingleMemberAnnotationExpr -> AnnotationType.SINGLE
            is MarkerAnnotationExpr -> AnnotationType.MARKER
            else -> null
        }

        this.pairs = when (annotationExpr) {
            is NormalAnnotationExpr -> annotationExpr.pairs
            else -> null
        }

        this.string = annotationExpr.toString()

        val compUnitOp = annotationExpr.findCompilationUnit()
        if (compUnitOp.isPresent) {
            val compUnit = compUnitOp.get()
            for (import in compUnit.imports) {
                this.imports += import.nameAsString
            }
        }
    }

    constructor(node: Node) {
        this.name = getAnnotationName(node)
        this.annotationType = null
        this.pairs = null
        this.string = node.textContent
        this.imports += getDocumentImports(node)
    }

    private fun getAnnotationName(node: Node): String {
        val nameNode = getTagNode(node, "name", true)!!
        return nameNode.textContent
    }

    private fun getDocumentImports(node: Node): MutableList<String> {
        val importList = mutableListOf<String>()
        val rootNode = getRootNode(node)
        val imports = getAllTagNodes(rootNode, "using", listOf("class", "function"))
        for (import in imports) {
            val importName = getTagNode(import, "name", true)!!
            importList += importName.textContent
        }

        return importList
    }
}

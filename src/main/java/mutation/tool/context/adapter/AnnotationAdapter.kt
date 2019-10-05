package mutation.tool.context.adapter

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.MemberValuePair
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import com.github.javaparser.utils.Pair
import mutation.tool.annotation.AnnotationType
import org.w3c.dom.Node

class AnnotationAdapter {

    val name:String
        get() = TODO("not implemented")
    val annotationType:AnnotationType?
        get() = TODO("not implemented")
    val imports: MutableList<String>
        get() = TODO("not implemented")
    val pairs: NodeList<MemberValuePair>
        get() = TODO("not implemented")
    val string: String
        get() = TODO("not implemented")

    constructor(annotationExpr: AnnotationExpr) {
        TODO("not implemented")
    }

    constructor(node: Node) {
        TODO("not implemented")
    }

}

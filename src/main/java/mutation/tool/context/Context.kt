package mutation.tool.context

import com.github.javaparser.ast.expr.AnnotationExpr

interface Context {
    fun getInsertionPoint(): InsertionPoint

    fun getAnnotations():List<AnnotationExpr> {
        return when(getInsertionPoint()) {
            InsertionPoint.PROPERTY -> (this as PropertyContext).entity.annotations
            InsertionPoint.METHOD -> (this as MethodContext).entity.annotations
            InsertionPoint.CLASS -> (this as ClassContext).entity.annotations
            InsertionPoint.PARAMETER -> (this as ParameterContext).entity.annotations
        }
    }
}
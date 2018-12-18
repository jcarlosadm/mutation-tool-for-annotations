package mutation.tool.annotation.context

import com.github.javaparser.ast.body.FieldDeclaration

class PropertyContext(val entity: FieldDeclaration):Context {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.PROPERTY
}
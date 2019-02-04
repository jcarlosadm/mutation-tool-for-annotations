package mutation.tool.context

import com.github.javaparser.ast.body.FieldDeclaration
import mutation.tool.context.entity.Entity

class PropertyContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.PROPERTY
}
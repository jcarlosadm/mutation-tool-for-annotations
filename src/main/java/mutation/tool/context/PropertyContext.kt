package mutation.tool.context

import com.github.javaparser.ast.body.FieldDeclaration
import mutation.tool.context.entity.Entity

/**
 * Context of Property type
 *
 * @param entity object with properties of context.
 * @constructor create a context of property type.
 */
class PropertyContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.PROPERTY
}
package mutation.tool.context

import com.github.javaparser.ast.body.MethodDeclaration
import mutation.tool.context.entity.Entity

/**
 * Context of Method type
 *
 * @param entity object with properties of context.
 * @constructor create a context of method type.
 */
class MethodContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.METHOD
}
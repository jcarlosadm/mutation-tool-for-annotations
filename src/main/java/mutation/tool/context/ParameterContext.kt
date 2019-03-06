package mutation.tool.context

import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.entity.Entity

/**
 * Context of Parameter type
 *
 * @param entity object with properties of context.
 * @constructor create a context of parameter type.
 */
class ParameterContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint {
        return InsertionPoint.PARAMETER
    }
}
package mutation.tool.context

import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.entity.Entity

class ParameterContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint {
        return InsertionPoint.PARAMETER
    }
}
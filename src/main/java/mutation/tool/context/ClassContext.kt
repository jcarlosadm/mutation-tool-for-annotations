package mutation.tool.context

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import mutation.tool.context.entity.Entity

/**
 * Context of Class type
 *
 * @param entity object with properties of context.
 * @constructor create a context of class type.
 */
class ClassContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.CLASS
}
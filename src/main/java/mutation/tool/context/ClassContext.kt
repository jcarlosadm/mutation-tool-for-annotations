package mutation.tool.context

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import mutation.tool.context.entity.Entity

/**
 * Context of Class type
 */
class ClassContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.CLASS
}
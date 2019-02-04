package mutation.tool.context

import com.github.javaparser.ast.body.MethodDeclaration
import mutation.tool.context.entity.Entity

class MethodContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.METHOD
}
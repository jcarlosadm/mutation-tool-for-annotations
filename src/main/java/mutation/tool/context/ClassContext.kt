package mutation.tool.context

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import mutation.tool.context.entity.Entity

class ClassContext(entity: Entity):Context(entity) {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.CLASS
}
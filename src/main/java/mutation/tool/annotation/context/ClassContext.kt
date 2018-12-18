package mutation.tool.annotation.context

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration

class ClassContext(val entity: ClassOrInterfaceDeclaration):Context {
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.CLASS
}
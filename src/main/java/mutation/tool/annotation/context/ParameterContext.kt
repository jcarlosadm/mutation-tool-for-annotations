package mutation.tool.annotation.context

import com.github.javaparser.ast.body.Parameter

class ParameterContext(val entity: Parameter):Context {
    override fun getInsertionPoint(): InsertionPoint {
        return InsertionPoint.PARAMETER
    }
}
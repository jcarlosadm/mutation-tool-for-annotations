package mutation.tool.annotation.visitor

import mutation.tool.context.Context
import java.io.File

interface VisitorStrategy {
    fun getContexts(file:File):List<Context>
}
package mutation.tool.context.visitor

import mutation.tool.context.Context

interface ContextCatcherVisitor {
    val contexts:MutableList<Context>
}
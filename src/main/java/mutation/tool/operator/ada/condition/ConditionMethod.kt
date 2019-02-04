package mutation.tool.operator.ada.condition

import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint

class ConditionMethod: Condition() {

    init {
        this.contextType = InsertionPoint.METHOD
    }

    override fun check(context: Context, isTarget: Boolean): Boolean {
        if (context.getInsertionPoint() != this.contextType || this.isTarget != isTarget) return false
        if (this.returnType != null && !checkReturnType(context)) return false
        if (this.accessModifier != null && !checkAccessModifier(context)) return false
        if (this.annotationNames != null && !checkAnnotationNames(context)) return false
        if (this.params != null && !checkParams(context)) return false

        return true
    }
}
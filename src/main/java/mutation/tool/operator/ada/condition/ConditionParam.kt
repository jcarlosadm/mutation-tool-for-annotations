package mutation.tool.operator.ada.condition

import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint

class ConditionParam: Condition() {

    init {
        this.contextType = InsertionPoint.PARAMETER
    }

    override fun check(context: Context, isTarget: Boolean): Boolean {
        if (this.contextType != context.getInsertionPoint() || this.isTarget != isTarget) return false
        if (this.type != null && !this.checkType(context)) return false
        if (this.annotationNames != null && !this.checkAnnotationNames(context)) return false

        return true
    }
}
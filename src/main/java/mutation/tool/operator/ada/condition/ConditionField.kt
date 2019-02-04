package mutation.tool.operator.ada.condition

import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint

class ConditionField: Condition() {

    init {
        this.contextType = InsertionPoint.PROPERTY
    }

    override fun check(context: Context, isTarget: Boolean): Boolean {
        if (context.getInsertionPoint() != this.contextType || this.isTarget != isTarget) return false
        if (this.type != null && !this.checkType(context)) return false
        if (this.accessModifier != null && !this.checkAccessModifier(context)) return false
        if (this.annotationNames != null && !this.checkAnnotationNames(context)) return false

        return true
    }
}
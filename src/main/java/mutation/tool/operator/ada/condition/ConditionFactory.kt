package mutation.tool.operator.ada.condition

import mutation.tool.context.InsertionPoint

class ConditionFactory {
    fun getInstance(
            contextType: InsertionPoint,
            isTarget: Boolean,
            accessModifier:String?,
            returnType:String?,
            type:String?,
            annotationNames:List<String>?,
            params:List<Param>?
    ): Condition = when(contextType) {
        InsertionPoint.CLASS -> ConditionClass().setIsTarget(isTarget).setAccessModifier(accessModifier).
                setAnnotations(annotationNames)
        InsertionPoint.METHOD -> ConditionMethod().setIsTarget(isTarget).setReturnType(returnType).
                setAccessModifier(accessModifier).setAnnotations(annotationNames).setParams(params)
        InsertionPoint.PROPERTY -> ConditionField().setIsTarget(isTarget).setType(type).
                setAccessModifier(accessModifier).setAnnotations(annotationNames)
        InsertionPoint.PARAMETER -> ConditionParam().setIsTarget(isTarget).setType(type).setAnnotations(annotationNames)
    }
}
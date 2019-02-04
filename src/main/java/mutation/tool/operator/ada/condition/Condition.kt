package mutation.tool.operator.ada.condition

import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint

abstract class Condition {
    protected var contextType = InsertionPoint.CLASS
    protected var isTarget = false
    protected var accessModifier:String? = null
    protected var returnType:String? = null
    protected var type:String? = null
    protected var annotationNames:List<String>? = null
    protected var params:List<Param>? = null

    fun setIsTarget(isTarget: Boolean): Condition {
        this.isTarget = isTarget
        return this
    }

    fun setAccessModifier(accessModifier: String?): Condition {
        this.accessModifier = accessModifier
        return this
    }

    fun setReturnType(returnType:String?):Condition {
        this.returnType = returnType
        return this
    }

    fun setType(type: String?): Condition {
        this.type = type
        return this
    }

    fun setAnnotations(annotations: List<String>?): Condition {
        this.annotationNames = annotations
        return this
    }

    fun setParams(params: List<Param>?): Condition {
        this.params = params
        return this
    }

    abstract fun check(context: Context, isTarget:Boolean):Boolean

    protected fun checkAnnotationNames(context: Context): Boolean {
        for (annotationName in this.annotationNames!!) {
            var found = false
            for (annotation in context.getAnnotations()) {
                if (annotation.nameAsString == annotationName) {
                    found = true
                    break
                }
            }

            if (!found) return false
        }

        return true
    }

    protected fun checkAccessModifier(context: Context): Boolean {
        val modifiers = context.getModifiers() ?: return false
        for (modifier in modifiers) {
            if (modifier.toString() == this.accessModifier) return true
        }

        return false
    }

    protected fun checkReturnType(context: Context): Boolean = context.getReturnType() == this.returnType

    protected fun checkParams(context: Context): Boolean {
        if (context.getParams() == null) return false

        for (param in this.params!!) {
            var found = false

            for (paramContext in context.getParams()!!) {
                if (param.type == paramContext.typeAsString) {
                    if (param.annotations != null) {
                        for (annotation in param.annotations!!) {
                            var found2 = false
                            for (paramAnnotation in paramContext.annotations) {
                                if (annotation == paramAnnotation.nameAsString) {
                                    found2 = true
                                    break
                                }
                            }

                            if (!found2) return false
                        }
                    }

                    found = true
                    break
                }
            }

            if(!found) return false
        }

        return true
    }

    protected fun checkType(context: Context): Boolean = context.getType() == this.type
}
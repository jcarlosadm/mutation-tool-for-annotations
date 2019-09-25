package mutation.tool.context.entity

import mutation.tool.context.InsertionPoint
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter

// TODO move to context package
interface Context {
    fun getName():String
    fun getBeginLine():Int
    fun getBeginColumn():Int
    fun getAnnotations():List<AnnotationAdapter>
    fun getAccessModifiers():List<ModifierAdapter>?
    fun getParameters():List<ParameterContext>?
    fun getReturnType():String?
    fun getType():String?
    fun getInsertionPoint():InsertionPoint
}
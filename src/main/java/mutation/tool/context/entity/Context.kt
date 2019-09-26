package mutation.tool.context.entity

import mutation.tool.context.InsertionPoint
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter

// TODO move to context package
interface Context {
    val name:String
    val beginLine:Int
    val beginColumn:Int
    val annotations:List<AnnotationAdapter>
    val accessModifiers:List<ModifierAdapter>?
    val parameters:List<ParameterContext>?
    val returnType:String?
    val type:String?

    fun getInsertionPoint():InsertionPoint
    override fun toString():String
}
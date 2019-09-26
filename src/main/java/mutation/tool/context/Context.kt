package mutation.tool.context

import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter

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
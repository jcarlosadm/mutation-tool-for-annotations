package mutation.tool.context.entity

// TODO rename to Entity after refactoring
interface Entity2 {
    fun getName():String
    fun getBeginLine():Int
    fun getBeginColumn():Int
    fun getAnnotations():List<AnnotationAdapter>
    fun getAccessModifiers():List<ModifierAdapter>?
    fun getParameters():List<ParameterEntity>?
    fun getReturnType():String?
    fun getType():String?
}
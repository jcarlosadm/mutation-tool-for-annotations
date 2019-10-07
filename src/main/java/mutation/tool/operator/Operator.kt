package mutation.tool.operator

interface Operator {
    fun checkContext():Boolean
    fun lock()
    fun unlock()
}
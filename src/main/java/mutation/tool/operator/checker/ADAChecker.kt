package mutation.tool.operator.checker

import mutation.tool.context.Context
import mutation.tool.operator.Operator
import java.io.File

class ADAChecker {
    private val tree:Node? = null

    fun buildTree(){
        TODO("not implemented")
    }

    fun check(contexts:List<Context>, javaFile:File):List<Operator> {
        TODO("not implemented")
    }
}

class Node(val currentValue: String?, val nextCheck:String?, val children:Map<String, Node>?, val result: Result?) {
}

class Result(val annotation: String, val numberOfConditions:Int) {
}
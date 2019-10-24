package mutation.tool.parser

import mutation.tool.context.InsertionPoint

interface AnnotationProcessor {
    val annotation: String
    fun process(insertionPoint: InsertionPoint)
}

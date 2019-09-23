package mutation.tool.parser

interface AnnotationProcessor {
    val annotation: String
    fun process()
}

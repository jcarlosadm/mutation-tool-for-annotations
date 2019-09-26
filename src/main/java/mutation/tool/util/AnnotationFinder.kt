package mutation.tool.util

import mutation.tool.context.adapter.AnnotationAdapter

/**
 * Determine if the one annotation is equal to some name
 *
 * @param annotation annotation to compare.
 * @param name name to compare. Must include the package.
 * @return true if the annotation is equal to name.
 */
fun annotationFinder(annotation:AnnotationAdapter, name:String):Boolean {
    val completeName = name.removePrefix("@")
    if (annotation.name == completeName || completeName.contains(annotation.name))
        return true
    return false
}
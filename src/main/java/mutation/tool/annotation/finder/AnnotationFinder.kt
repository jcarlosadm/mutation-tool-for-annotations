package mutation.tool.annotation.finder

import mutation.tool.context.adapter.AnnotationAdapter

/**
 * Determine if the one annotation is equal to some name
 *
 * @param annotation annotation to compare.
 * @param name name to compare. Must include the package.
 * @return true if the annotation is equal to name.
 */
fun javaAnnotationFinder(annotation: AnnotationAdapter, name:String):Boolean {
    var name = name.removePrefix("@")
    val imports = annotation.imports
    if (annotation.name == name) return true

    if (!name.contains(annotation.name)) return false

    name = (name.removeSuffix(".${annotation.name}"))

    for (import in imports) {
        if (import.contains(name)) return true
        else if (import.contains("*")) {
            val importCleaned = import.removeSuffix(".*")
            if (name.contains(importCleaned)) return true
        }
    }

    return false
}

/**
 * Determine if the one annotation is equal to some name
 *
 * @param annotation annotation to compare.
 * @param name name to compare. Must include the package.
 * @return true if the annotation is equal to name.
 */
fun cSharpAnnotationFinder(annotation: AnnotationAdapter, name: String): Boolean {
    TODO("not implemented")
}
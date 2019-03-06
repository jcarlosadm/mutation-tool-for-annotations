package mutation.tool.util

import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.expr.AnnotationExpr

/**
 * Determine if the one annotation is equal to some name
 *
 * @param annotation annotation to compare.
 * @param name name to compare. Must include the package.
 * @param imports imports of the file.
 * @return true if the annotation is equal to name.
 */
fun annotationFinder(annotation:AnnotationExpr, name:String):Boolean {
    var name = name.removePrefix("@")
    val imports = annotation.findCompilationUnit().get().imports
    if (annotation.nameAsString == name) return true

    if (!name.contains(annotation.nameAsString)) return false

    name = (name.removeSuffix(".${annotation.nameAsString}"))

    for (import in imports) {
        if (import.nameAsString.contains(name)) return true
        else if (import.nameAsString.contains("*")) {
            val importCleaned = import.nameAsString.removeSuffix(".*")
            if (name.contains(importCleaned)) return true
        }
    }

    return false
}
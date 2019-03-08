package mutation.tool.util.json

import mutation.tool.context.InsertionPoint

/**
 * Information about an annotation
 * 
 * @param name name of the annotation.
 * @param replaceableBy current annotation can be replaceable by annotations of this list.
 * @param targets possible targets (locations in file) of this annotation.
 * @param attributes list of attributes of this annotation
 * @constructor creates a unit of information about an annotation
 */
class AnnotationInfo (
        val name:String,
        val replaceableBy:List<String>,
        val targets:List<InsertionPoint>,
        val attributes:List<Attribute>
) {
    /**
     * prebuild string of this annotation
     */
    var annotationStrings:List<String>
        private set

    init {
        val strings = mutableListOf<String>()

        if (attributes.isEmpty()) strings += name

        for (attr in attributes) {
            if (attr.default) {
                for (value in attr.validValues) {
                    strings += "$name($value)"
                }
            }

            for (value in attr.validValues) {
                strings += "$name(${attr.name} = $value)"
            }
        }

        annotationStrings = strings
    }
}
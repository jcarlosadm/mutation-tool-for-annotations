package mutation.tool.util.json

import mutation.tool.context.InsertionPoint

class AnnotationInfo (
        val name:String,
        val replaceableBy:List<String>,
        val targets:List<InsertionPoint>,
        val attributes:List<Attribute>
) {
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
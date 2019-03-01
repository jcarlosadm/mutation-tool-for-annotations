package mutation.tool.util.json

import mutation.tool.context.InsertionPoint

class AnnotationInfo (
        val name:String,
        val replaceableBy:List<String>,
        val targets:List<InsertionPoint>,
        val attributes:List<Attribute>
) {}
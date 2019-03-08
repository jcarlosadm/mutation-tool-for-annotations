package mutation.tool.util.json

/**
 * Information of an attribute of one annotation
 * 
 * @param name name of the attribute.
 * @param type type of the attribute.
 * @param validValues valid values of the attribute.
 * @param default if this is the default attribute of the annotation.
 * @constructor create an information about an attribute.
 */
class Attribute(
        val name:String,
        val type:String,
        val validValues:List<String>,
        val default:Boolean
) {}
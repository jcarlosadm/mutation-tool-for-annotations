package mutation.tool.annotation.builder

import mutation.tool.util.Language
import mutation.tool.util.xml.codeToDocument
import org.w3c.dom.Node

class CSharpAnnotationBuilder(override val stringRepresentation: String) :AnnotationBuilder {

    var node:Node? = null
        private set

    override fun build() {
        this.node = codeToDocument(this.stringRepresentation, Language.C_SHARP).firstChild
    }
}
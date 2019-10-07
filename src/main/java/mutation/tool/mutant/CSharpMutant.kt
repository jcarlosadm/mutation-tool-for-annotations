package mutation.tool.mutant

import mutation.tool.operator.OperatorsEnum
import org.w3c.dom.Node

class CSharpMutant(override val operator: OperatorsEnum):Mutant {

    lateinit var rootNode: Node

    override fun toString(): String = rootNode.childNodes.item(0).textContent
}
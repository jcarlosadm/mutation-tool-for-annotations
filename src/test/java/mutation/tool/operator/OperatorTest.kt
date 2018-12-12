package mutation.tool.operator

import mutation.tool.annotation.AnnotationContext
import mutation.tool.mutant.Mutant
import org.junit.jupiter.api.Assertions.*

class RemoveController:Operator {
    override fun checkContext(context: AnnotationContext): Boolean {
        TODO("not implemented")
    }

    override fun mutate(context: AnnotationContext):Mutant? {
        TODO("not implemented")
    }
}

class ChangeRequestMappingAttr:Operator {
    override fun checkContext(context: AnnotationContext): Boolean {
        TODO("not implemented")
    }

    override fun mutate(context: AnnotationContext):Mutant? {
        TODO("not implemented")
    }
}

internal class OperatorTest {

}
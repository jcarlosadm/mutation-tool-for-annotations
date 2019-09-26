package mutation.tool.operator.rma

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.mutant.Mutant
import mutation.tool.operator.FILE1
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RMATest {

    @Test
    fun testRMA() {
        val mutants = mutableListOf<Mutant>()

        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            val operator = RMA(context, File(FILE1))

            val annotations = context.getAnnotations()
            if (annotations.isNotEmpty()) {
                assertTrue(operator.checkContext())

                mutants.addAll(operator.mutate())
            } else {
                assertFalse(operator.checkContext())
            }
        }
        assertEquals(11, mutants.size)
    }
}
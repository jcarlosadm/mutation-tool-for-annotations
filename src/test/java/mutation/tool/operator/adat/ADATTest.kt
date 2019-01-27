package mutation.tool.operator.adat

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.mutant.Mutant
import mutation.tool.operator.FILE1
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class ADATTest {

    private val map = mapOf(
            "RequestMapping" to listOf(
                    mapOf(
                            "name" to "value",
                            "type" to "String"
                    ),
                    mapOf(
                            "name" to "method",
                            "type" to "RequestMethod"
                    )
            )
    )

    @Test
    fun testADAT() {
        val mutants = mutableListOf<Mutant>()

        for (context in getListOfAnnotationContext(File(FILE1))) {
            val operator = ADAT(context, File(FILE1))
            operator.map = map
            if (operator.checkContext()) mutants += operator.mutate()
        }

        assertEquals(0, mutants.size)
    }
}
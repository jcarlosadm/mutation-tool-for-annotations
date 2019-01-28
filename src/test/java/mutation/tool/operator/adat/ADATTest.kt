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
                            "value" to "\"/ex/foos\"",
                            "asSingle" to "true"
                    ),
                    mapOf(
                            "name" to "method",
                            "value" to "RequestMethod.POST"
                    ),
                    mapOf(
                            "name" to "headers",
                            "value" to "{\"key1=val1\", \"key2=val2\"}"
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

        assertEquals(13, mutants.size)
    }
}
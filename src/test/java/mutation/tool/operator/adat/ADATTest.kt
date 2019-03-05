package mutation.tool.operator.adat

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.mutant.Mutant
import mutation.tool.operator.FILE1
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class ADATTest {

    @Test
    fun testADAT() {
        val mutants = mutableListOf<Mutant>()

        val builder = ADATMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()
        val map = builder.map

        for (context in getListOfAnnotationContext(File(FILE1))) {
            val operator = ADAT(context, File(FILE1))
            operator.map = map
            if (operator.checkContext()) mutants += operator.mutate()
        }

        assertEquals(13, mutants.size)
    }
}
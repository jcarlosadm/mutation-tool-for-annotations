package mutation.tool.operator.rmat

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.FILE1
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RMATTest {

    @Test
    fun testRMAT() {
        val mutants = mutableListOf<JavaMutant>()
        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            val operator = JavaRMAT(context, File(FILE1))

            var count = 0
            for (annotation in context.annotations) {
                if (annotation.string.contains(Regex("\\((.*?)\\)"))) count++
            }

            if (count > 0) {
                assertTrue(operator.checkContext())
                mutants.addAll(operator.mutate())
            } else {
                assertFalse(operator.checkContext())
            }
        }

        assertEquals(10, mutants.size)
    }
}
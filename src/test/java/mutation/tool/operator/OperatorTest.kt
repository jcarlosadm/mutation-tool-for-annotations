package mutation.tool.operator

import mutation.tool.annotation.context.Context
import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.mutant.Mutant
import mutation.tool.util.getAnnotations
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.File

private const val FILE1 = "./src/test/resources/fakeProject/src/main/java/TarefasController.java"

internal class OperatorTest {

    companion object {
        private var contexts:List<Context>? = null

        @BeforeAll
        @JvmStatic
        internal fun getContexts() {
            contexts = getListOfAnnotationContext(File(FILE1))
        }
    }

    @Test
    fun testRMA() {
        val mutants = mutableListOf<Mutant>()
        for (context in contexts!!) {
            val operator:Operator = RMA(context, File(FILE1))

            val annotations = getAnnotations(context)
            if (annotations.isNotEmpty()) {
                assertTrue(operator.checkContext())

                mutants.addAll(operator.mutate())
            } else {
                assertFalse(operator.checkContext())
            }
        }
        assertEquals(10, mutants.size)
    }

    @Test
    fun testRMAT() {
        val mutants = mutableListOf<Mutant>()
        for (context in contexts!!) {
            val operator:Operator = RMAT(context, File(FILE1))

            var count = 0
            for (annotation in getAnnotations(context)) {
                if (annotation.toString().contains(Regex("\\((.*?)\\)"))) count++
            }

            if (count > 0) {
                assertTrue(operator.checkContext())
                mutants.addAll(operator.mutate())
            } else {
                assertFalse(operator.checkContext())
            }
        }

        assertEquals(9, mutants.size)
    }
}
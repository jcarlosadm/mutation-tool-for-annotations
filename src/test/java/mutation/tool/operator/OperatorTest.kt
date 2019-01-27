package mutation.tool.operator

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.context.InsertionPoint
import mutation.tool.mutant.Mutant
import mutation.tool.operator.ada.ADA
import mutation.tool.operator.chodr.CHODR
import mutation.tool.operator.rma.RMA
import mutation.tool.operator.rmat.RMAT
import mutation.tool.operator.rpa.RPA
import mutation.tool.operator.swtg.SWTG
import mutation.tool.util.MutationToolConfig
import mutation.tool.util.getAnnotations
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

const val FILE1 = "./src/test/resources/fakeProject/src/main/java/TarefasController.java"

internal class OperatorTest {

    @Test
    fun testGetValidOperators() {
        val config = MutationToolConfig(File(""), File(""))
        config.operators += listOf(OperatorsEnum.RMA, OperatorsEnum.RMAT)
        val validOperators = getValidOperators(getListOfAnnotationContext(File(FILE1)), File(FILE1), config)

        assertEquals(19, validOperators.size)
    }

    @Test
    fun testRMA() {
        val mutants = mutableListOf<Mutant>()

        for (context in getListOfAnnotationContext(File(FILE1))) {
            val operator = RMA(context, File(FILE1))

            val annotations = getAnnotations(context)
            if (annotations.isNotEmpty()) {
                assertTrue(operator.checkContext())

                mutants.addAll(operator.mutate())
            } else {
                assertFalse(operator.checkContext())
            }
        }
        assertEquals(11, mutants.size)
    }

    @Test
    fun testRMAT() {
        val mutants = mutableListOf<Mutant>()
        for (context in getListOfAnnotationContext(File(FILE1))) {
            val operator = RMAT(context, File(FILE1))

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

        assertEquals(10, mutants.size)
    }

    @Test
    fun testADA() {
        val annotations = listOf(
                "@a",
                "@a(\"ww\")",
                "@a(5)",
                "@a(file=\"err\", name = \"ee\")"
        )

        val mutants = mutableListOf<Mutant>()
        for (context in getListOfAnnotationContext(File(FILE1))) {
            val operator = ADA(context, File(FILE1))
            operator.annotation = annotations[(0..(annotations.size - 1)).random()]
            mutants += operator.mutate()
        }

        assertEquals(19, mutants.size)
    }

    @Test
    fun testCHODR() {
        val mutants = mutableListOf<Mutant>()

        for(context in getListOfAnnotationContext(File(FILE1))) {
            val operator = CHODR(context, File(FILE1))
            if (operator.checkContext())
                mutants += operator.mutate()
        }

        assertEquals(1, mutants.size)
    }

    @Test
    fun testRPA() {
        val mutants = mutableListOf<Mutant>()
        val contexts = getListOfAnnotationContext(File(FILE1))
        val map = mapOf(
                "Autowired" to listOf("@a", "@b(1)", "@c(name=\"aaa\")"),
                "Qualifier" to listOf("@rrrr")
        )
        val importMap = mapOf(
                "@a" to "mutationtool.test.annotation.a",
                "@b" to "mutationtool.test.annotation.b",
                "@c" to "mutationtool.test.annotation.c",
                "@rrrr" to "mutationtool.test.annotation.rrrr"
        )

        for (context in contexts) {
            val operator = RPA(context, File(FILE1))
            operator.switchMap = map
            operator.importMap = importMap
            if (operator.checkContext())
                mutants += operator.mutate()
        }

        assertEquals(4, mutants.size)
    }

    @Test
    fun testSWTG() {
        val mutants = mutableListOf<Mutant>()
        val contexts = getListOfAnnotationContext(File(FILE1))
        val map = mapOf(
                "Autowired" to listOf(InsertionPoint.PROPERTY, InsertionPoint.METHOD),
                "RequestMapping" to listOf(InsertionPoint.CLASS, InsertionPoint.METHOD)
        )

        for (context in contexts) {
            val operator = SWTG(context, File(FILE1), contexts)
            operator.mapContextType = map
            if (operator.checkContext())
                mutants += operator.mutate()
        }

        assertEquals(14, mutants.size)
    }
}
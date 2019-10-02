package mutation.tool.operator.ada

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.FILE1
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class ADATest {

    @Test
    fun testADA() {
        val annotations = listOf(
                "@a",
                "@a(\"ww\")",
                "@a(5)",
                "@a(file=\"err\", name = \"ee\")"
        )

        val mutants = mutableListOf<JavaMutant>()
        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            val operator = ADA(context, File(FILE1))
            operator.annotation = annotations[(0..(annotations.size - 1)).random()]
            mutants += operator.mutate()
        }

        assertEquals(19, mutants.size)
    }

    @Test
    fun testADAWithJavafile() {
        val mutants = mutableListOf<JavaMutant>()
        val adaChecker = ADAChecker(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        adaChecker.build()

        for(operator in adaChecker.check(getListOfAnnotationContext(File(FILE1), JavaStrategy()), File(FILE1))) {
            if(operator.checkContext()) mutants += operator.mutate()
        }

        assertEquals(22, mutants.size)
    }
}
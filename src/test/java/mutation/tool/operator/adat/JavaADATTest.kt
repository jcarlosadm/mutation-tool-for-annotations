package mutation.tool.operator.adat

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.FILE1
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class JavaADATTest {

   /* @Test
    fun testADAT() {
        val mutants = mutableListOf<JavaMutant>()

        val builder = ADATMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()
        val map = builder.map

        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            val operator = JavaADAT(context, File(FILE1))
            operator.map = map
            if (operator.checkContext()) mutants += operator.mutate()
        }

        assertEquals(13, mutants.size)
    }*/
}
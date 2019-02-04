package mutation.tool.util

import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.getListOfAnnotationContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

private const val FILE1 = "./src/test/resources/fakeProject/src/main/java/TarefasController.java"

internal class InspectorTest {

    @Test
    fun testGetAnnotations() {
        val annotations = mutableListOf<AnnotationExpr>()

        for (context in getListOfAnnotationContext(File(FILE1))) {
            annotations.addAll(context.getAnnotations())
        }

        assertTrue(annotations.isNotEmpty())
        assertEquals(11, annotations.size)
    }

}
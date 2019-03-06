package mutation.tool.util

import mutation.tool.annotation.getListOfAnnotationContext
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

internal class AnnotationFinderKtTest {

    @Test
    fun testAnnotationFinder() {
        val names = listOf(
                "@org.springframework.web.bind.annotation.RequestMapping",
                "@org.springframework.validation.Valid",
                "@org.springframework.beans.factory.annotation.Autowired",
                "@org.springframework.beans.factory.annotation.Qualifier",
                "@org.springframework.stereotype.Controller")

        val contexts = getListOfAnnotationContext(
                File("./src/test/resources/fakeProject/src/main/java/TarefasController3.java"))
        for (context in contexts) {
            for (annotation in context.getAnnotations()) {
                val index = when(annotation.nameAsString) {
                    "RequestMapping" -> 0
                    "Qualifier" -> 3
                    "org.springframework.beans.factory.annotation.Qualifier" -> 3
                    "Autowired" -> 2
                    "Valid" -> 1
                    "stereotype.Controller" -> 4
                    else -> -1
                }

                assertTrue(annotationFinder(annotation, names[index]))
            }
        }
    }
}
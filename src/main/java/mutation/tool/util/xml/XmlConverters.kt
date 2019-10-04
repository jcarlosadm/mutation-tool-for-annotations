package mutation.tool.util.xml

import mutation.tool.util.Language
import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun fileToDocument(file:File, language: Language):Document {
    val process = getProcess("srcml " + file.absolutePath + " --position -l \"" + language.string + "\"")
    return getDocument(process)!!
}

fun codeToDocument(code:String, language: Language):Document {
    val process = getProcess("srcml -t \"" + code + "\" + l + \"" + language.string + "\"")
    return getDocument(process)!!
}

private fun getDocument(process: Process): Document? {
    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    return builder.parse(process.inputStream)
}

private fun getProcess(command:String): Process {
    val process = Runtime.getRuntime().exec(command)
    process.waitFor()
    return process
}
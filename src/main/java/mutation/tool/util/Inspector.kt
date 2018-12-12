package mutation.tool.util

import org.apache.commons.io.FileUtils
import java.io.File

fun getAllJavaFiles(dir:File):List<File> = FileUtils.listFiles(dir, arrayOf("java"), true).toList()

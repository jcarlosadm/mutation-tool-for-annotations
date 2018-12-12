package mutation.tool.util

import java.io.File

private const val TEMP_FOLDER = "temp"

private const val DATA_FOLDER = "data"

private const val REPORT_FOLDER = "report"

/**
 * Make basic root folders of Mutation Tool project
 */
fun makeRootFolders():Boolean {
    TODO("not implemented")
}

fun deleteTempFolder():Boolean {
    TODO("not implemented")
}

fun isSubpath(folder1:File, folder2:File):Boolean = (folder1.absolutePath.contains(folder2.absolutePath) ||
        folder2.absolutePath.contains(folder1.absolutePath))

package mutation.tool.util

import java.io.File

const val TEMP_FOLDER = "temp"
const val DATA_FOLDER = "data"
const val REPORT_FOLDER = "report"

private fun makeFolderIfNotExists(folder:File):Boolean = ((folder.exists() && folder.isDirectory) || folder.mkdirs())

/**
 * Make basic root folders of Mutation Tool project
 */
fun makeRootFolders():Boolean = (makeFolderIfNotExists(File("./$TEMP_FOLDER")) &&
        makeFolderIfNotExists(File("./$DATA_FOLDER")) && makeFolderIfNotExists(File("./$REPORT_FOLDER")))

/**
 * delete temporary folder, and all his contents
 */
fun deleteTempFolder():Boolean {
    TODO("not implemented")
}

/**
 * Check if folder1 is subfolder of folder2, and vice versa
 *
 * @param folder1: first folder
 * @param folder2: second folder
 */
fun isSubpath(folder1:File, folder2:File):Boolean = (folder1.absolutePath.contains(folder2.absolutePath) ||
        folder2.absolutePath.contains(folder1.absolutePath))

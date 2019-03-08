package mutation.tool.util

import org.apache.commons.io.FileUtils
import java.io.File

const val TEMP_FOLDER = "temp"
const val DATA_FOLDER = "data"
const val MUTANTS_FOLDER = "$DATA_FOLDER/mutants"
const val REPORT_FOLDER = "report"

private fun makeFolderIfNotExists(folder:File):Boolean = ((folder.exists() && folder.isDirectory) || folder.mkdirs())

/**
 * Make basic root folders of Mutation Tool project
 * 
 * @return true if the root folders are created
 */
fun makeRootFolders():Boolean = (makeFolderIfNotExists(File("./$TEMP_FOLDER")) &&
        makeFolderIfNotExists(File("./$DATA_FOLDER")) && makeFolderIfNotExists(File(
        "./$REPORT_FOLDER")) && makeFolderIfNotExists(File(MUTANTS_FOLDER)))

/**
 * delete temporary folder, and all his contents
 * 
 * @return true if the temp folders are deleted
 */
fun deleteTempFolder():Boolean = File("./$TEMP_FOLDER").deleteRecursively()

/**
 * Check if folder1 is subfolder of folder2, and vice versa
 *
 * @param folder1 first folder
 * @param folder2 second folder
 * @return true if folder1 is subfolder of folder2, and folder2 is subfolder of folder1
 */
fun isSubFolder(folder1:File, folder2:File):Boolean = (folder1.absolutePath.contains(folder2.absolutePath) ||
        folder2.absolutePath.contains(folder1.absolutePath))

/**
 * Get all java files of a subfolder, recursively
 * 
 * @return list of Java files
 */
fun getAllJavaFiles(dir: File):List<File> = FileUtils.listFiles(dir, arrayOf("java"), true).toList()
package mutation.tool.mutant

import mutation.tool.project.Project
import java.io.File

class Mutant(folder:File):Project(folder) {
	val mutatedFiles = mutableListOf<MutatedFile>()
}
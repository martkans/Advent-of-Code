import model.Directory
import model.File
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val commandLineOutput =
            Utils.readFile(Path("calendar-2022/day-07-no-space-left-on-device/src/main/resources/data.txt"))

        val root = buildFileSystem(commandLineOutput)
        root.calculateSize()


        println("Result for part I: ${root.getSumOfSizesOfAllCategoriesExactlyOrBelowLimit(100000)}")
        println("Result for part II: ${findDirectoryToDelete(70000000, 30000000, root)}")
    }

    private fun buildFileSystem(commandLineOutput: List<String>): Directory {
        val root = Directory("/", null, mutableListOf())
        var currentDirectory: Directory = root
        commandLineOutput.forEach {
            if (it.startsWith("$")) {
                with(it.split(" ")) {
                    if (this[1] == "cd") {
                        currentDirectory = when (this[2]) {
                            "/" -> root
                            ".." -> currentDirectory.parent!!
                            else -> currentDirectory.objects.find { this[2] == it.name } as Directory
                        }
                    }
                }
            } else {
                with(it.split(" ")) {
                    if (this[0] == "dir") {
                        currentDirectory.objects.add(Directory(this[1], currentDirectory, mutableListOf()))
                    } else {
                        currentDirectory.objects.add(File(this[1], currentDirectory, this[0].toLong()))
                    }
                }
            }
        }
        return root
    }

    private fun findDirectoryToDelete(diskSize: Long, needSpace: Long, root: Directory) =
        root.getSmallestDirectoryToDelete(needSpace - diskSize + root.calculatedSize!!)
}
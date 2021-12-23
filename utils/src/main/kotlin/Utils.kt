import java.nio.file.Files
import java.nio.file.Path

object Utils {

    fun readFile(path: Path): List<String> = Files.readAllLines(path)

    fun List<String>.splitLineBy(delimeter: String = " "): List<List<String>> = this.map { it.split(delimeter) }
}
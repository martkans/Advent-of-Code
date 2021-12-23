import java.nio.file.Files
import java.nio.file.Path

object FileReader {

    fun readFile(path: Path) = Files.readAllLines(path)
}
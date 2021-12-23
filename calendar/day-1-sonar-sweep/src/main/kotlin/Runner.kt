import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val lines = FileReader.readFile(Path("calendar/day-1-sonar-sweep/src/main/resources/data.txt"))
        val result = countLargerMeasurements(lines)
        println(result)
    }
    
    private fun countLargerMeasurements(lines: List<String>): Int = lines.map{ it.toInt() }
        .foldIndexed(0) { idx, acc, element ->
            if (idx < 1 || element <= lines[idx-1].toInt()) acc
            else acc + 1
        }
}
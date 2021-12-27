import kotlin.io.path.Path
import models.Record

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.readFile(Path("calendar/day-08-seven-segment-search/src/main/resources/data.txt"))

        val records = lines.map {
            val temp = it.split(" | ")
            Record(temp[0].split(' '), temp[1].split(' '))
        }

        println("Number of easy digits: ${countEasyDigits(records)}")
    }

    private fun countEasyDigits(records: List<Record>) = records.flatMap { it.outputs }.count {
        val distinctLetters = countDistinctLetters(it)
        distinctLetters == 2 || distinctLetters == 3 || distinctLetters == 4 || distinctLetters == 7
    }

    private fun countDistinctLetters(s: String): Int = s.chars().distinct().count().toInt()
}
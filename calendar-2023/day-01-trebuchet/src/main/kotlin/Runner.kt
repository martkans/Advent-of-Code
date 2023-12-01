import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-01-trebuchet/src/main/resources/data.txt"))

        println("Result for I: ${data.sumOf { getNumberFromLine(it) }}")
        println("Result for II: ${data.sumOf { getNumberFromLineInIntelligentWay(it) }}")
    }

    private val DIGITS_MAP = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )
    private val DIGITS = DIGITS_MAP.keys.toList() + DIGITS_MAP.values.toList()

    private fun getNumberFromLine(line: String): Int =
        "${line.find { it.isDigit() }}${line.findLast { it.isDigit() }}".toInt()

    private fun getNumberFromLineInIntelligentWay(line: String): Int =
        "${line.findAnyOf(DIGITS)?.second.let { DIGITS_MAP[it] ?: it }}${line.findLastAnyOf(DIGITS)?.second.let { DIGITS_MAP[it] ?: it }}".toInt()
}
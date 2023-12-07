import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-06-wait-for-it/src/main/resources/data.txt"))

        val races = extractRaceData(data)
        println("Result for I: ${races.productOfAllBeatRecordPossibilities()}")

        val singleRace = extractRaceDataJoined(data)
        println("Result for II: ${singleRace.countBeatRecordPossibilities()}")

    }

    private val NUM_REGEX = Regex("\\d+")

    private fun extractRaceData(lines: List<String>): List<Race> {
        val times = NUM_REGEX.findAll(lines.first()).map { it.value.toLong() }
        val distances = NUM_REGEX.findAll(lines.last()).map { it.value.toLong() }

        return times.zip(distances) { time, distance ->
            Race(time, distance)
        }.toList()
    }

    private fun extractRaceDataJoined(lines: List<String>): Race {
        val time = NUM_REGEX.findAll(lines.first()).map { it.value }.joinToString("").toLong()
        val distance = NUM_REGEX.findAll(lines.last()).map { it.value }.joinToString("").toLong()

        return Race(time, distance)
    }

    private fun List<Race>.productOfAllBeatRecordPossibilities() =
        this.map { it.countBeatRecordPossibilities() }.fold(1) { acc, n -> acc * n }

    private fun Race.countBeatRecordPossibilities(): Int = (1 until time).count { it * (time - it) > distance }

    data class Race(
        val time: Long,
        val distance: Long
    )
}
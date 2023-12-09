import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-09-mirage-maintenance/src/main/resources/data.txt"))
            .map { it.split(" ").map { it.toLong() } }

        println("Result for I: ${data.sumOfExtrapolatedValues()}")
        println("Result for II: ${data.sumOfPreviousExtrapolatedValues()}")

    }

    private fun List<List<Long>>.sumOfExtrapolatedValues() = this.sumOf { it.nextValueInHistory()}
    private fun List<List<Long>>.sumOfPreviousExtrapolatedValues() = this.sumOf { it.previousValueInHistory()}
    private fun List<Long>.nextValueInHistory(): Long {
        var historyLevel = 0
        var nextValueInHistory = 0L
        val historySets = mutableListOf(this)
        val temp = mutableListOf<Long>()
        while (historySets[historyLevel].any { it != 0L }) {
            historyLevel += 1
            for (i in (1 until historySets[historyLevel - 1].size)) {
                temp.add(historySets[historyLevel - 1][i] - historySets[historyLevel - 1][i - 1])
            }
            historySets.add(listOf(*temp.toTypedArray()))
            temp.clear()
        }

        while (historyLevel > 0) {
            historyLevel -= 1
            nextValueInHistory += historySets[historyLevel].last()
        }

        return nextValueInHistory
    }

    private fun List<Long>.previousValueInHistory(): Long {
        var historyLevel = 0
        var previousValueInHistory = 0L
        val historySets = mutableListOf(this)
        val temp = mutableListOf<Long>()
        while (historySets[historyLevel].any { it != 0L }) {
            historyLevel += 1
            for (i in (1 until historySets[historyLevel - 1].size)) {
                temp.add(historySets[historyLevel - 1][i] - historySets[historyLevel - 1][i - 1])
            }
            historySets.add(listOf(*temp.toTypedArray()))
            temp.clear()
        }

        while (historyLevel > 0) {
            historyLevel -= 1
            previousValueInHistory = historySets[historyLevel].first() - previousValueInHistory
        }

        return previousValueInHistory
    }

}
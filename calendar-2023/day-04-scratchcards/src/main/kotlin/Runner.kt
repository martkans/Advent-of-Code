import kotlin.io.path.Path
import kotlin.math.pow

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-04-scratchcards/src/main/resources/data.txt"))
        val cards = data.map { extractCardData(it) }

        println("Result for I: ${calculateScratchCardPoints(cards)}")

        val cardsMap = cards.associateBy { it.id }
        cardsMap.playGame()

        println("Result for II: ${cardsMap.countCards()}")
    }

    private val NUM_REGEX = Regex("\\d+")

    private fun extractCardData(line: String) = Card(
        id = NUM_REGEX.find(line.substringBefore(":"))!!.value.toInt(),
        winningNumbers = NUM_REGEX.findAll(line.substring(line.indexOf(":"), line.indexOf("|")))
            .map { it.value.toInt() }.toSet(),
        yourNumbers = NUM_REGEX.findAll(line.substringAfter("|")).map { it.value.toInt() }.toSet(),
    )

    private fun calculateScratchCardPoints(cards: List<Card>) = cards.sumOf {
        val cardWinningNumbersCount = it.countWinningNumbers()
        if (cardWinningNumbersCount > 0) {
            2.0.pow(cardWinningNumbersCount - 1.0)
        } else 0.0
    }.toInt()

    private fun Map<Int, Card>.playGame() {
        this.values.forEach {
            incrementCopiesOfNNextCardsWithXNumber(it.id, it.countWinningNumbers(), it.copies)
        }
    }

    private fun Map<Int, Card>.incrementCopiesOfNNextCardsWithXNumber(originalId: Int, n: Int, x: Int) {
        ((originalId + 1)..(originalId + n)).forEach {
            this[it]?.let { it.copies += x }
        }
    }
    private fun Map<Int, Card>.countCards() = this.values.sumOf { it.copies }

    data class Card(
        val id: Int,
        val winningNumbers: Set<Int>,
        val yourNumbers: Set<Int>,
        var copies: Int = 1
    ) {
        fun countWinningNumbers(): Int = yourNumbers.intersect(winningNumbers).size
    }

}
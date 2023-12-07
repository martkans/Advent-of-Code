import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-07-camel-cards/src/main/resources/data.txt"))
        val hands = extractHands(data)

        println("Result for I: ${hands.getTotalWinnings()}")
        println("Result for II: ${hands.getTotalWinningsWithJoker()}")
    }

    private val orderOfCards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    private val orderOfCardsWithJoker = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')


    private val handComparatorWithJoker = object : Comparator<Hand> {
        override fun compare(other1: Hand, other2: Hand): Int {
            val groups = other1.hand.groupingBy { it }.eachCount().toMutableMap()
            val otherGroups = other2.hand.groupingBy { it }.eachCount().toMutableMap()

            if (groups.containsKey('J') && groups.size != 1) {
                val joker = groups.remove('J')!!
                val key = groups.filterValues { it == groups.maxOf { it.value } }.keys.first()
                groups[key] = groups[key]!!.plus(joker)

            }

            if (otherGroups.containsKey('J') && otherGroups.size != 1) {
                val joker = otherGroups.remove('J')!!
                val key = otherGroups.filterValues { it == otherGroups.maxOf { it.value } }.keys.first()
                otherGroups[key] = otherGroups[key]!!.plus(joker)
            }

            val maxGroupsCount = groups.maxOf { it.value }
            val otherMaxGroupsCount = otherGroups.maxOf { it.value }

            if (maxGroupsCount > otherMaxGroupsCount) return 1
            if (maxGroupsCount < otherMaxGroupsCount) return -1

            if (groups.size < otherGroups.size) return 1
            if (groups.size > otherGroups.size) return -1

            other1.hand.zip(other2.hand).forEach {
                if (orderOfCardsWithJoker.indexOf(it.first) < orderOfCardsWithJoker.indexOf(it.second)) return 1
                if (orderOfCardsWithJoker.indexOf(it.first) > orderOfCardsWithJoker.indexOf(it.second)) return -1
            }

            return 0
        }

    }

    private fun extractHands(lines: List<String>) = lines.map { it.split(" ").let { Hand(it.first(), it.last().toInt()) } }

    private fun List<Hand>.getTotalWinnings(): Long = this.sorted().let {
        it.foldIndexed(0) { idx, acc, hand -> acc + (idx + 1) * hand.bid }
    }

    private fun List<Hand>.getTotalWinningsWithJoker(): Long = this.sortedWith(handComparatorWithJoker).let {
        it.foldIndexed(0) { idx, acc, hand -> acc + (idx + 1) * hand.bid }
    }

    data class Hand(
        val hand: String,
        val bid: Int
    ): Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            val groups = this.hand.groupingBy { it }.eachCount()
            val otherGroups = other.hand.groupingBy { it }.eachCount()

            val maxGroupsCount = groups.maxOf { it.value }
            val otherMaxGroupsCount = otherGroups.maxOf { it.value }

            if (maxGroupsCount > otherMaxGroupsCount) return 1
            if (maxGroupsCount < otherMaxGroupsCount) return -1

            if (groups.size < otherGroups.size) return 1
            if (groups.size > otherGroups.size) return -1

            this.hand.zip(other.hand).forEach {
                if (orderOfCards.indexOf(it.first) < orderOfCards.indexOf(it.second)) return 1
                if (orderOfCards.indexOf(it.first) > orderOfCards.indexOf(it.second)) return -1
            }

            return 0
        }
    }
}
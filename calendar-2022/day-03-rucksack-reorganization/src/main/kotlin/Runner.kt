import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val rucksacks = Utils.readFile(Path("calendar-2022/day-03-rucksack-reorganization/src/main/resources/data.txt"))

        val rucksacksPriorities = getRucksacksPriorities(rucksacks)
        val sumOfRucksacksPriorities = calculateSumOfPriorities(rucksacksPriorities)

        println("Result for part I: $sumOfRucksacksPriorities")


        val elvesBadges = getElvesGroupBadge(rucksacks)
        val elvesBadgesPrioritiesSum = calculateSumOfPriorities(elvesBadges)
        println("Result for part II: $elvesBadgesPrioritiesSum")
    }

    private fun getRucksacksPriorities(rucksacks: List<String>) =
        rucksacks.map { it.subSequence(0, it.length / 2) to it.subSequence(it.length / 2, it.length) }
            .flatMap { it.first.toSet().intersect(it.second.toSet()) }

    private fun calculateSumOfPriorities(priorities: List<Char>) =
        priorities.sumOf { if (it.isLowerCase()) it.code - 96 else it.code - 38 }

    private fun getElvesGroupBadge(rucksacks: List<String>, groupSize: Int = 3) = rucksacks.chunked(groupSize)
        .flatMap {
            var commonItems = it[0].toHashSet()
            for (i in 1 until groupSize) {
                commonItems = commonItems.intersect(it[i].toSet()).toHashSet()
            }
            commonItems
        }
}
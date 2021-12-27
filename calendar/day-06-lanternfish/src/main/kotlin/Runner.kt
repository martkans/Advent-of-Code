import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val lanternfishes = Utils.readFile(Path("calendar/day-06-lanternfish/src/main/resources/data.txt"))[0]
            .split(',').map { Lanternfish(it.toInt(), false) }

        val finalPopulation = simulateLanternfishesPopulation(lanternfishes, 80)

        println("Final population: ${finalPopulation.size}")
    }

    private fun simulateLanternfishesPopulation(initialPopulation: List<Lanternfish>, days: Int): List<Lanternfish> {
        var day = 0
        val population = mutableListOf<Lanternfish>()
        population.addAll(initialPopulation)
        printLanternfishPopulation(population, day)
        while (++day <= days) {
            processToNextDay(population)
            bornNewLanternfishes(population)
        }

        return population
    }

    private fun printLanternfishPopulation(population: List<Lanternfish>, day: Int) =
        println("After day $day: ${population.map { it.daysToNew }.joinToString()}")

    private fun processToNextDay(lanternfishes: List<Lanternfish>) = lanternfishes.forEach{ it.daysToNew-- }
    private fun bornNewLanternfishes(lanternfishes: MutableList<Lanternfish>) {
        var numberOfFishesToAdd = 0
        lanternfishes.forEach{
            if (it.daysToNew < 0) {
                it.apply {
                    daysToNew = 6
                    isNewLanternfish = false
                }
                numberOfFishesToAdd++
            }
        }

        (1..numberOfFishesToAdd).forEach { _ -> lanternfishes.add(Lanternfish()) }
    }

}
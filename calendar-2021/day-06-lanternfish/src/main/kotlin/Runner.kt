import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val lanternfishesByDay = Utils.readFile(Path("calendar-2021/day-06-lanternfish/src/main/resources/data.txt"))[0]
            .split(',').map { it.toInt() }.groupBy { it }

        val numOfLanternfishesByDay = MutableList(9) { 0L }
        lanternfishesByDay.forEach { (k, v) -> numOfLanternfishesByDay[k] = v.size.toLong() }

        simulateLanternfishesPopulation(numOfLanternfishesByDay, 256)
        println("Final population: ${numOfLanternfishesByDay.sum()}")
    }

    private fun simulateLanternfishesPopulation(population: MutableList<Long>, days: Int) {
        var day = 0
        while (++day <= days) {
            val numOfFishesToReproduce = population.removeAt(0)
            population.add(numOfFishesToReproduce)
            population[6] += numOfFishesToReproduce
        }
    }
}
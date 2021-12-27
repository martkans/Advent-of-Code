import kotlin.io.path.Path
import kotlin.math.abs

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val crabSubmarinesPositions = Utils.readFile(Path("calendar/day-07-the-treachery-of-whales/src/main/resources/data.txt"))[0]
            .split(',').map { it.toInt() }

        val minFuel = findPositionWithLeastFuelAmount(crabSubmarinesPositions)

        println("Minimal amount of fuel to set crab submarines on specific position: $minFuel")
    }

    private fun findPositionWithLeastFuelAmount(initPositions: List<Int>): Int? {
        val min = initPositions.minOrNull()
        val max = initPositions.maxOrNull()

        return (min!!..max!!).map {
            calculateAmountOfFuelNeededToMoveToPosition(it, initPositions)
        }.minOrNull()
    }

    private fun calculateAmountOfFuelNeededToMoveToPosition(position: Int, initPositions: List<Int>) =
        initPositions.sumOf { abs(it - position) }
}
import kotlin.io.path.Path
import kotlin.math.abs

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val crabSubmarinesPositions = Utils.readFile(Path("calendar-2021/day-07-the-treachery-of-whales/src/main/resources/data.txt"))[0]
            .split(',').map { it.toInt() }

        val minFuel = findPositionWithLeastFuelAmount(crabSubmarinesPositions)
        println("Minimal amount of fuel to set crab submarines on specific position: $minFuel")

        val minFuelAdvanced = findPositionWithLeastFuelAmount(crabSubmarinesPositions, true)
        println("Minimal amount of fuel with advanced consumption to set crab submarines on specific position: $minFuelAdvanced")
    }

    private fun findPositionWithLeastFuelAmount(initPositions: List<Int>, advancedFuelConsumption: Boolean = false): Int? {
        val min = initPositions.minOrNull()
        val max = initPositions.maxOrNull()

        return (min!!..max!!).map {
            calculateAmountOfFuelNeededToMoveToPosition(it, initPositions, advancedFuelConsumption)
        }.minOrNull()
    }

    private fun calculateAmountOfFuelNeededToMoveToPosition(position: Int, initPositions: List<Int>,
                                                            advancedFuelConsumption: Boolean) =
        initPositions.sumOf {
            if(advancedFuelConsumption) calculateAdvancedFuelConsumption(position, it) else abs(it - position)
        }

    private fun calculateAdvancedFuelConsumption(destination: Int, initPosition: Int) =
        (1..abs(destination - initPosition)).sum()
}
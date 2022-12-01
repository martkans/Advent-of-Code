import models.LifeSupportRates
import models.PowerRates
import kotlin.io.path.Path
import kotlin.math.ceil

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val binaries = Utils.readFile(Path("calendar-2021/day-03-binary-diagnostic/src/main/resources/data.txt"))

        val powerRates = calculatePowerRates(binaries)
        println("Gamma rate: ${powerRates.gammaRate.toInt(2)}, epsilon rate: ${powerRates.epsilonRate.toInt(2)}. " +
                "Result: ${powerRates.gammaRate.toInt(2) * powerRates.epsilonRate.toInt(2)}")

        val lifeSupportRates = calculateLifeSupportRates(binaries)
        println("Oxygen rate: ${lifeSupportRates.oxygenGeneratorRate.toInt(2)}, CO2 rate: ${lifeSupportRates.co2ScrubberRate.toInt(2)}. " +
                "Result: ${lifeSupportRates.oxygenGeneratorRate.toInt(2) * lifeSupportRates.co2ScrubberRate.toInt(2)}")
    }

    private fun calculatePowerRates(binaries: List<String>): PowerRates {
        val oneCounter = MutableList(binaries[0].length) {0}
        binaries.forEach { it.forEachIndexed { index, c -> if(c == '1') oneCounter[index]++ } }

        val gammaRate: List<Byte> = oneCounter.map { if (it > binaries.size/2) 1 else 0 }
        val epsilonRate: List<Byte> = oneCounter.map { if (it < binaries.size/2) 1 else 0 }

        return PowerRates(gammaRate.joinToString(""), epsilonRate.joinToString(""))
    }

    private fun calculateLifeSupportRates(binaries: List<String>): LifeSupportRates =
        LifeSupportRates(getLifeSupportRate(binaries, true), getLifeSupportRate(binaries, false))

    private fun getLifeSupportRate(binaries: List<String>, isOxygen: Boolean): String {
        var temp = binaries.map { it }
        var idx = 0

        do {
            val determinedChar = determineCharAtIndex(temp, idx, isOxygen)
            temp = temp.filter { it[idx] == determinedChar }
            idx++
        } while (idx < binaries[0].length && temp.size != 1)

        return temp[0]
    }


    private fun determineCharAtIndex(binaries: List<String>, idx: Int, isOxygen: Boolean): Char {
        var counter = 0
        binaries.forEach { if (it[idx] == '1') counter++ }
        val condition = counter >= ceil(binaries.size/2f)
        return if(isOxygen) {
            if (condition) '1' else '0'
        } else {
            if (condition) '0' else '1'
        }
    }
}
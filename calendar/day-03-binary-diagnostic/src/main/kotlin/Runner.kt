import models.Rates
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val binaries = Utils.readFile(Path("calendar/day-03-binary-diagnostic/src/main/resources/data.txt"))
        val rates = calculateRates(binaries)

        println("Gamma rate: ${rates.gammaRate.toInt(2)}, epsilon rate: ${rates.epsilonRate.toInt(2)}. " +
                "Result: ${rates.gammaRate.toInt(2) * rates.epsilonRate.toInt(2)}")
    }

    private fun calculateRates(binaries: List<String>): Rates {
        val oneCounter = MutableList(binaries[0].length) {0}
        binaries.forEach { it.forEachIndexed { index, c -> if(c == '1') oneCounter[index]++ } }

        val gammaRate: List<Byte> = oneCounter.map { if (it > binaries.size/2) 1 else 0 }
        val epsilonRate: List<Byte> = oneCounter.map { if (it < binaries.size/2) 1 else 0 }

        return Rates(gammaRate.joinToString(""), epsilonRate.joinToString(""))
    }
}
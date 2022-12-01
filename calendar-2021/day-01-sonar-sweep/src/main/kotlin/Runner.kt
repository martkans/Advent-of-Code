import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val measurements = Utils.readFile(Path("calendar-2021/day-01-sonar-sweep/src/main/resources/data.txt"))
            .map { it.toInt() }

        val result = countLargerMeasurements(measurements)
        println("Result for part I: $result")


        val resultSliderWindow = countLargerMeasurementsSliderWindow(measurements, 3)
        println("Result for part II: $resultSliderWindow")
    }
    
    private fun countLargerMeasurements(measurements: List<Int>): Int = measurements
        .foldIndexed(0) { idx, acc, element ->
            if (idx < 1 || element <= measurements[idx-1]) acc
            else acc + 1
        }

    private fun countLargerMeasurementsSliderWindow(measurements: List<Int>, windowSize: Int): Int = measurements
        .foldIndexed(0) { idx, acc, _ ->
            if (idx < windowSize
                || measurements.subList(idx - windowSize + 1, idx + 1).sum() <= measurements.subList(idx - windowSize, idx).sum()
            ) acc
            else acc + 1
        }
}
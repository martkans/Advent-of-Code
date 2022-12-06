import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val signal =
            Utils.readFile(Path("calendar-2022/day-06-tuning-trouble/src/main/resources/data.txt"))[0]

        println("Result for part I: ${getIndexOfStartOfPacket(signal)}")
        println("Result for part II: ${getIndexOfStartOfMessagePacket(signal)}")
    }

    private fun getIndexOfStartOfPacket(signal: String) = getIndexOfFirstNotRepeated(signal, 4)
    private fun getIndexOfStartOfMessagePacket(signal: String) = getIndexOfFirstNotRepeated(signal, 14)

    private fun getIndexOfFirstNotRepeated(signal: String, notRepeatedCount: Int): Int {
        for (i in notRepeatedCount - 1 until signal.length) {
            if (signal.substring(i - (notRepeatedCount - 1), i + 1).toSet().size == notRepeatedCount) return i + 1
        }
        return -1
    }

}
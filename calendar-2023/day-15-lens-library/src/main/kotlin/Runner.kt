import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val sequences =
            Utils.readFile(Path("calendar-2023/day-15-lens-library/src/main/resources/data.txt")).first().split(",")

        println("Result for I: ${sequences.sumOf { it.calculateHolidayAsciiStringHelper() }}")
        println("Result for II: ${sequences.calculateFocusingPower()}")
    }

    private fun String.calculateHolidayAsciiStringHelper() =
        this.fold(0) { acc: Int, c: Char -> (acc + c.code) * 17 % 256 }

    private fun List<String>.calculateFocusingPower(): Long {
        val boxes = MutableList<LinkedHashMap<String, Int>>(256) { LinkedHashMap() }
        this.forEach {
            if (it.contains('-')) {
                it.substringBefore('-').let { label ->
                    boxes[label.calculateHolidayAsciiStringHelper()].remove(label)
                }
            } else {
                it.split('=').let { (label, lensPower) ->
                    boxes[label.calculateHolidayAsciiStringHelper()].put(label, lensPower.toInt())
                }
            }
        }

        return boxes.mapIndexed { boxIndex, linkedHashMap ->
            linkedHashMap.values.mapIndexed { index, value -> (boxIndex + 1) * (index + 1) * value }.sum()
        }.sum().toLong()
    }
}

import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-03-gear-ratios/src/main/resources/data.txt"))

        println("Result for I: ${getPartNumbersFromSchema(data).sum()}")
        println("Result for II: ${getGearRatiosFromSchema(data).sum()}")
    }

    private val NUM_REGEX = Regex("\\d+")
    private val SYMBOL_REGEX = Regex("[^\\w\\d\\.]")

    private fun getPartNumbersFromSchema(schema: List<String>): List<Int> {
        val partNumbers = mutableListOf<Int>()
        for (i in schema.indices) {
            when (i) {
                0 -> partNumbers.addAll(getPartNumberFrom(schema[i], null, schema[i + 1]))
                schema.lastIndex -> partNumbers.addAll(getPartNumberFrom(schema[i], schema[i - 1], null))
                else -> partNumbers.addAll(getPartNumberFrom(schema[i], schema[i - 1], schema[i + 1]))
            }
        }

        return partNumbers
    }

    private fun getPartNumberFrom(line: String, lineAbove: String? = null, lineBelow: String? = null): List<Int> {
        val numbers = NUM_REGEX.findAll(line)

        return numbers.filter { match ->
            val startRange = match.range.first
            val endRange = match.range.last
            val startIndex = maxOf(startRange - 1, 0)
            val endIndex = minOf(endRange + 2, line.length)
            when {
                startRange > 0 && line[startRange - 1] != '.' -> true
                endRange < line.length - 1 && line[endRange + 1] != '.' -> true
                lineAbove != null && lineAbove.substring(startIndex, endIndex).contains(SYMBOL_REGEX) -> true
                lineBelow != null && lineBelow.substring(startIndex, endIndex).contains(SYMBOL_REGEX) -> true
                else -> false
            }


        }.map { it.value.toInt() }.toList()
    }

    private fun getGearRatiosFromSchema(schema: List<String>): List<Int> {
        val gearRatios = mutableListOf<Int>()
        for (i in schema.indices) {
            when (i) {
                0 -> gearRatios.addAll(getGearRatiosFromLine(schema[i], null, schema[i + 1]))
                schema.lastIndex -> gearRatios.addAll(getGearRatiosFromLine(schema[i], schema[i - 1], null))
                else -> gearRatios.addAll(getGearRatiosFromLine(schema[i], schema[i - 1], schema[i + 1]))
            }
        }

        return gearRatios
    }

    private fun getGearRatiosFromLine(line: String, lineAbove: String? = null, lineBelow: String? = null): List<Int> {
        val possibleGears = Regex("\\*").findAll(line)
        if (possibleGears.toList().isEmpty()) return emptyList()

        val parts = NUM_REGEX.findAll(line).toMutableList()
        lineAbove?.let { parts.addAll(NUM_REGEX.findAll(it).toMutableList()) }
        lineBelow?.let { parts.addAll(NUM_REGEX.findAll(it).toMutableList()) }

        return possibleGears.map { gear ->
            val connectedParts = parts.filter { it.range.last >= (gear.range.first - 1) && it.range.first <= (gear.range.last + 1)}.map { it.value.toInt() }
            if (connectedParts.size == 2) connectedParts[0] * connectedParts[1] else null
        }.filterNotNull().toList()
    }

}
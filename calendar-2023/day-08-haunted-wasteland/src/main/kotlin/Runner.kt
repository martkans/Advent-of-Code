import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-08-haunted-wasteland/src/main/resources/data.txt"))
        val instructions = data[0]
        val nodes = extractNodes(data.subList(2, data.size))

        println("Result for I: ${nodes.followInstructionsForKey("AAA", instructions) { key -> key == "ZZZ"}}")
        println("Result for II: ${nodes.followInstructionsLikeGhost(instructions)}")

    }

    private fun extractNodes(lines: List<String>) =
        lines.associate { it.substring(0, 3) to Node(it.substring(7, 10), it.substring(12, 15)) }

    data class Node(
        val left: String,
        val right: String
    )

    private fun Map<String, Node>.findAllStartingKeys() = this.keys.filter { it.last() == 'A' }

    private fun Map<String, Node>.followInstructionsLikeGhost(instructions: String): Long {
        val keys = this.findAllStartingKeys()
        val stepsForEachKey = keys.map {
            this.followInstructionsForKey(it, instructions) {key -> key.endsWith('Z') }
        }
        var allLeastCommonMultiple = stepsForEachKey[0]

        for (i in 1 until stepsForEachKey.size) {
            allLeastCommonMultiple = leastCommonMultiple(allLeastCommonMultiple, stepsForEachKey[i])
        }

        return allLeastCommonMultiple
    }

    private fun Map<String, Node>.followInstructionsForKey(
        startKey: String,
        instructions: String,
        endPredicate: (key: String) -> Boolean
    ): Long {
        var stepsPerformed = 0L
        var instructionIndex = 0
        var actualKey = startKey

        while (!endPredicate.invoke(actualKey)) {
            actualKey = when (instructions[instructionIndex]) {
                'R' -> this[actualKey]!!.right
                else -> this[actualKey]!!.left
            }

            instructionIndex = (instructionIndex + 1).mod(instructions.length)
            stepsPerformed += 1
        }

        return stepsPerformed
    }

    private fun leastCommonMultiple(a: Long, b: Long) = (a * b) / greatestCommonDivisor(a, b)

    private fun greatestCommonDivisor(a: Long, b: Long): Long {
        var num1 = a
        var num2 = b
        while (num2 != 0L) {
            val temp = num2
            num2 = num1 % num2
            num1 = temp
        }

        return num1
    }
}
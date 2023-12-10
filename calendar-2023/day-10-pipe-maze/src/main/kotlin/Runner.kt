import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val maze = Utils.readFile(Path("calendar-2023/day-10-pipe-maze/src/main/resources/data.txt"))

        println("Result for I: ${maze.findCycle().size / 2}")
    }

    private val UP_POSSIBILITIES = setOf('7', '|', 'F')
    private val RIGHT_POSSIBILITIES = setOf('J', '-', '7')
    private val DOWN_POSSIBILITIES = setOf('J', '|', 'L')
    private val LEFT_POSSIBILITIES = setOf('L', '-', 'F')

    private fun List<String>.findCycle(): List<Char> {
        var (prev, next, nextIncomingDirection, i, j) = findStartWithSymbol()
        val path = mutableListOf(prev)

        while (next != 'S') {

            path.add(next)

            when (next) {
                '|' -> if (nextIncomingDirection == DIRECTION.DOWN) i -= 1 else i += 1

                '-' -> if (nextIncomingDirection == DIRECTION.RIGHT) j -= 1 else j += 1

                '7' -> if (nextIncomingDirection == DIRECTION.LEFT) {
                    i += 1
                    nextIncomingDirection = DIRECTION.UP
                } else {
                    j -= 1
                    nextIncomingDirection = DIRECTION.RIGHT
                }

                'F' -> if (nextIncomingDirection == DIRECTION.RIGHT) {
                    i += 1
                    nextIncomingDirection = DIRECTION.UP
                } else {
                    j += 1
                    nextIncomingDirection = DIRECTION.LEFT
                }

                'J' -> if (nextIncomingDirection == DIRECTION.LEFT) {
                    i -= 1
                    nextIncomingDirection = DIRECTION.DOWN
                } else {
                    j -= 1
                    nextIncomingDirection = DIRECTION.RIGHT
                }

                'L' -> if (nextIncomingDirection == DIRECTION.RIGHT) {
                    i -= 1
                    nextIncomingDirection = DIRECTION.DOWN
                } else {
                    j += 1
                    nextIncomingDirection = DIRECTION.LEFT
                }

                else -> error("should not happen")
            }

            next = this[i][j]
        }

        return path.toList()
    }


    private fun List<String>.findStartWithSymbol(): NextStep {
        for (i in this.indices) {
            this[i].indexOf('S').let {
                if (it != -1) {
                    val up = if (i > 0 && UP_POSSIBILITIES.contains(this[i - 1][it])) this[i - 1][it] else null // UP
                    val right =
                        if (it < this[i].length - 1 && RIGHT_POSSIBILITIES.contains(this[i][it + 1])) this[i][it + 1] else null // RIGHT
                    val down =
                        if (i < this.size - 1 && DOWN_POSSIBILITIES.contains(this[i + 1][it])) this[i + 1][it] else null// DOWN
                    val left =
                        if (it > 0 && LEFT_POSSIBILITIES.contains(this[i][it - 1])) this[i][it - 1] else null // LEFT

                    return when {
                        up != null && down != null -> NextStep('|', up, DIRECTION.DOWN, i - 1, it)
                        up != null && right != null -> NextStep('L', up, DIRECTION.DOWN, i - 1, it)
                        up != null && left != null -> NextStep('J', up, DIRECTION.DOWN, i - 1, it)
                        left != null && right != null -> NextStep('-', left, DIRECTION.RIGHT, i, it - 1)
                        left != null && down != null -> NextStep('7', left, DIRECTION.RIGHT, i, it - 1)
                        right != null && down != null -> NextStep('F', right, DIRECTION.LEFT, i, it + 1)
                        else -> error("should not happen")
                    }
                }
            }
        }

        error("should not happen")
    }

    enum class DIRECTION {
        UP, RIGHT, DOWN, LEFT
    }

    data class NextStep(
        val prev: Char,
        val nextSymbol: Char,
        val nextIncomingDirection: DIRECTION,
        val i: Int,
        val j: Int
    )
}

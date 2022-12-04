import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val rounds = Utils.readFile(Path("calendar-2022/day-02-rock-paper-scissors/src/main/resources/data.txt"))
            .map { it.split(" ").let { it[0] to it[1] } }

        println("Result for part I: ${getTotalScoreTurn(rounds)}")
        println("Result for part II: ${getTotalScoreResult(rounds)}")
    }


    private val OPPONENT_TURN = mapOf(
        "A" to 1, // rock
        "B" to 2, // paper
        "C" to 3  // scissors
    )

    private val MY_TURN = mapOf(
        "X" to 1,
        "Y" to 2,
        "Z" to 3
    )

    private val MY_RESULT = mapOf(
        "X" to 0,
        "Y" to 3,
        "Z" to 6
    )


    private fun getTotalScoreTurn(rounds: List<Pair<String, String>>) = rounds
        .map { OPPONENT_TURN[it.first]!! to MY_TURN[it.second]!! }
        .let { it.sumOf { it.second + areYouWinningSon(it) } }

    private fun areYouWinningSon(round: Pair<Int, Int>): Int = with(round) {
        if (first == second) {
            3
        } else if (
            second == 2 && first == 1
            || second == 3 && first == 2
            || second == 1 && first == 3
        ) {
            6
        } else {
            0
        }
    }

    private fun getTotalScoreResult(rounds: List<Pair<String, String>>) = rounds
        .map { OPPONENT_TURN[it.first]!! to MY_RESULT[it.second]!! }
        .let { it.sumOf { it.second + getScoreOfTurn(it) } }

    private fun getScoreOfTurn(round: Pair<Int, Int>): Int = with(round) {
        when (second) {
            3 -> first
            6 -> when (first) {
                1 -> 2
                2 -> 3
                else -> 1
            }

            else -> when (first) {
                1 -> 3
                2 -> 1
                else -> 2
            }
        }
    }
}
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val rounds = Utils.readFile(Path("calendar-2022/day-02-rock-paper-scissors/src/main/resources/data.txt"))
            .map { it.split(" ").let { it[0] to it[1] } }

        val roundsPoints: List<Pair<Int, Int>> = rounds.map { opponent[it.first]!! to my[it.second]!! }


        val score = roundsPoints.sumOf {
            it.second + areYouWinningSon(it)
        }

        val roundsResult: List<Pair<Int, Int>> = rounds.map { opponent[it.first]!! to my2[it.second]!! }

        val score2 = roundsResult.sumOf {
            toScore(it) + it.second
        }


        println("Result for part I: $score")
        println("Result for part II: $score2")
    }


    val opponent = mapOf(
        "A" to 1, // rock
        "B" to 2, // paper
        "C" to 3  // scissors
    )

    val my = mapOf(
        "X" to 1,
        "Y" to 2,
        "Z" to 3
    )

    val my2 = mapOf(
        "X" to 0,
        "Y" to 3,
        "Z" to 6
    )

    val score = mapOf(
        "WIN" to 6,
        "DRAW" to 3,
        "LOSE" to 0
    )

    fun areYouWinningSon(round: Pair<Int, Int>): Int = with(round) {
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

    fun toScore(round: Pair<Int, Int>): Int = with(round) {
        if(second == 3) {
            first
        } else if(second == 6) {
            if(first == 1) {
                2
            } else if(first == 2) {
                3
            } else {
                1
            }
        } else {
            if(first == 1) {
                3
            } else if(first == 2) {
                1
            } else {
                2
            }
        }
    }
}
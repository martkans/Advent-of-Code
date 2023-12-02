import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-02-cube-conundrum/src/main/resources/data.txt"))
        val games = data.map { it.extractGameData() }


        println("Result for I: ${sumOfPossibleGamesIds(games)}")
        println("Result for II: ${sumOfPowersSmallestSetPerGame(games)}")
    }

    private fun sumOfPossibleGamesIds(games: List<Game>): Int =
        games.filter { it.isGamePossible(GameSet(12, 13, 14)) }.sumOf { it.id }

    private fun Game.isGamePossible(set: GameSet): Boolean {
        this.sets.forEach {
            if (it.blueCubes > set.blueCubes || it.greenCubes > set.greenCubes || it.redCubes > set.redCubes)
                return false
        }
        return true
    }

    private fun sumOfPowersSmallestSetPerGame(games: List<Game>) =
        games.sumOf { it.getSmallestSetOfCubesToPlay().let { it.blueCubes * it.greenCubes * it.redCubes } }

    private fun Game.getSmallestSetOfCubesToPlay() =
        GameSet(
            redCubes = this.sets.maxOf { it.redCubes },
            blueCubes = this.sets.maxOf { it.blueCubes },
            greenCubes = this.sets.maxOf { it.greenCubes }
        )


    private fun String.extractGameData(): Game =
        this.split(":").let {game ->
            Game(
                id = game[0].substringAfter("Game ").toInt(),
                sets = game[1].split(";").map {set ->
                    val cubes = set.split(",").map { it.trim().split(" ") }
                    GameSet(
                        redCubes = cubes.filter { it[1] == "red" }.sumOf { it[0].toInt() },
                        greenCubes = cubes.filter { it[1] == "green" }.sumOf { it[0].toInt() },
                        blueCubes = cubes.filter { it[1] == "blue" }.sumOf { it[0].toInt() }
                    )
                }
            )
        }

    data class Game(
        val id: Int,
        val sets: List<GameSet> = emptyList()
    )

    data class GameSet(
        val redCubes: Int = 0,
        val greenCubes: Int = 0,
        val blueCubes: Int = 0
    )
}
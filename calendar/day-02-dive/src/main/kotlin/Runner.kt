import Utils.splitLineBy
import models.Command
import models.Direction
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val commands = Utils.readFile(Path("calendar/day-02-dive/src/main/resources/data.txt"))
            .splitLineBy()
            .map { Command(Direction.valueOf(it[0].uppercase()), it[1].toInt()) }

        val submarinePosition = moveSubmarine(commands)
        println("Position of submarine is (${submarinePosition.first}, ${submarinePosition.second}) " +
                "and product of position is ${submarinePosition.first * submarinePosition.second}")
    }

    private fun moveSubmarine(commands: List<Command>) : Pair<Int, Int> = commands
        .fold(Pair(0, 0)) { acc, command ->
            when(command.direction) {
                Direction.DOWN -> Pair(acc.first, acc.second + command.value)
                Direction.UP -> Pair(acc.first, acc.second - command.value)
                Direction.FORWARD -> Pair(acc.first + command.value, acc.second)
            }
        }
}
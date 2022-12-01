import Utils.splitLineBy
import models.Command
import models.Coordinates
import models.Direction
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val commands = Utils.readFile(Path("calendar-2021/day-02-dive/src/main/resources/data.txt"))
            .splitLineBy()
            .map { Command(Direction.valueOf(it[0].uppercase()), it[1].toInt()) }

        val submarinePosition: Coordinates = moveSubmarine(commands)
        println("Position of submarine is (${submarinePosition.x}, ${submarinePosition.y}) " +
                "and product of position is ${submarinePosition.x * submarinePosition.y}")

        val submarinePositionWithAim: Coordinates = moveSubmarineWithAim(commands)
        println("Position of submarine is (${submarinePositionWithAim.x}, ${submarinePositionWithAim.y}, ${submarinePositionWithAim.aim}) " +
                "and product of position is ${submarinePositionWithAim.x * submarinePositionWithAim.y}")
    }

    private fun moveSubmarine(commands: List<Command>) : Coordinates = commands
        .fold(Coordinates()) { acc, command ->
            when(command.direction) {
                Direction.DOWN -> Coordinates(acc.x, acc.y + command.value)
                Direction.UP -> Coordinates(acc.x, acc.y - command.value)
                Direction.FORWARD -> Coordinates(acc.x + command.value, acc.y)
            }
        }

    private fun moveSubmarineWithAim(commands: List<Command>) : Coordinates = commands
        .fold(Coordinates(aim = 0)) { acc, command ->
            when(command.direction) {
                Direction.DOWN -> Coordinates(acc.x, acc.y, acc.aim!! + command.value)
                Direction.UP -> Coordinates(acc.x, acc.y, acc.aim!! - command.value)
                Direction.FORWARD -> Coordinates(acc.x + command.value, acc.y + acc.aim!! * command.value, acc.aim)
            }
        }
}
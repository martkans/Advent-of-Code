import Utils.splitLineBy
import model.Addx
import model.Command
import model.Noop
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val commands =
            Utils.readFile(Path("calendar-2022/day-10-cathode-ray-tube/src/main/resources/data.txt"))
                .splitLineBy(" ").map { if (it[0] == "noop") Noop() else Addx(it[1].toInt()) }

        val registerStatesAfterCommands = performCommands(commands)

        println(
            "Result for part I: ${
                getSumOfSignalStrengths(
                    listOf(20, 60, 100, 140, 180, 220),
                    registerStatesAfterCommands
                )
            }"
        )
        println("Result for part II:")
        drawMessageOnCrt(registerStatesAfterCommands)
    }

    private fun performCommands(commands: List<Command>): List<Int> {
        val registerStates = mutableListOf(1)

        commands.forEach {
            when (it) {
                is Noop -> registerStates.add(registerStates.last())
                is Addx -> registerStates.addAll(listOf(registerStates.last(), registerStates.last() + it.value))
            }
        }

        return registerStates
    }

    private fun getSumOfSignalStrengths(signals: List<Int>, registerStates: List<Int>) =
        signals.sumOf { it * registerStates[it - 1] }

    private fun drawMessageOnCrt(registerStates: List<Int>) {
        (0 until registerStates.size - 1).map {
            if (it % 40 in (registerStates[it] - 1..registerStates[it] + 1)) "#" else "."
        }.chunked(40)
            .forEach { println(it.joinToString(separator = "")) }
    }
}
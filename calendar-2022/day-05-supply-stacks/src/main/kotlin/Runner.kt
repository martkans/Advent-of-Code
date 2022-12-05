import Utils.splitLineBy
import model.Step
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data =
            Utils.readFile(Path("calendar-2022/day-05-supply-stacks/src/main/resources/data.txt"))

        val cargoCrateMover9000 = loadCargoData(data)
        val cargoCrateMover9001 = loadCargoData(data)
        val instructions = loadInstructions(data)

        moveCratesWithCrateMover9000(instructions, cargoCrateMover9000)
        moveCratesWithCrateMover9001(instructions, cargoCrateMover9001)

        println("Result for part I: ${getLastCrates(cargoCrateMover9000)}")
        println("Result for part II: ${getLastCrates(cargoCrateMover9001)}")
    }

    private fun getLastCrates(cargo: List<ArrayDeque<String>>) = cargo.joinToString("") { it.last() }
    private fun moveCratesWithCrateMover9000(instructions: List<Step>, cargo: List<ArrayDeque<String>>) = instructions
        .forEach { performStepWithCrateMover9000(it, cargo) }

    private fun performStepWithCrateMover9000(step: Step, cargo: List<ArrayDeque<String>>) {
        repeat((0 until step.move).count()) {
            val crate = cargo[step.from - 1].removeLast()
            cargo[step.to - 1].addLast(crate)
        }
    }

    private fun moveCratesWithCrateMover9001(instructions: List<Step>, cargo: List<ArrayDeque<String>>) = instructions
        .forEach { performStepWithCrateMover9001(it, cargo) }

    private fun performStepWithCrateMover9001(step: Step, cargo: List<ArrayDeque<String>>) {
        val tempStack = ArrayDeque<String>()
        repeat((0 until step.move).count()) {
            val crate = cargo[step.from - 1].removeLast()
            tempStack.addLast(crate)
        }

        repeat((0 until step.move).count()) {
            cargo[step.to - 1].addLast(tempStack.removeLast())
        }
    }

    private fun getIndexOfBreakerLine(data: List<String>) = data.map { it.trim() }.indexOf("")

    private fun loadInstructions(data: List<String>) = data.subList(getIndexOfBreakerLine(data) + 1, data.size)
        .splitLineBy(" ")
        .map { Step(it[1].toInt(), it[3].toInt(), it[5].toInt()) }

    private fun loadCargoData(data: List<String>): List<ArrayDeque<String>> {
        val cargo = mutableListOf<ArrayDeque<String>>()
        val dataForCargo = data.subList(0, getIndexOfBreakerLine(data) - 1).asReversed()

        cargo.addAll((0 until dataForCargo[0].chunked(4).size).map { ArrayDeque() })
        dataForCargo.forEach {
            it.chunked(4)
                .forEachIndexed { index, crate ->
                    if (crate.trim() != "") {
                        cargo[index].addLast(crate.trim().removePrefix("[").removeSuffix("]"))
                    }
                }
        }
        return cargo
    }
}
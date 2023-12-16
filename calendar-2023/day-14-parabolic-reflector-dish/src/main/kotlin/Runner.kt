import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val parabolicReflectorDish = Utils.readFile(Path("calendar-2023/day-14-parabolic-reflector-dish/src/main/resources/data.txt"))
        val tiltedToNorth = parabolicReflectorDish.tiltToNorth()
        println("Result I: ${tiltedToNorth.sumOfLoadedRocks()}")
    }

    private fun List<String>.transposed(): List<String> {
        val transposed = mutableListOf<String>()
        for (i in this.first().indices) {
            transposed += this.map { it[i] }.joinToString("")
        }
        return transposed
    }

    private fun List<String>.tiltToNorth(): List<String> = this.transposed().map { it.moveRocks() }.transposed()

    private fun String.moveRocks(): String {
        val tilted = StringBuilder(this)
        var destination = this.indexOf('.')
        var i = destination + 1
        do {
            if (tilted[i] == 'O') {
                tilted[destination] = 'O'
                tilted[i] = '.'
                destination = tilted.indexOf('.', destination)
                i += 1
            } else if (tilted[i] == '#') {
                destination = tilted.indexOf('.', i)
                i = destination + 1
            } else i += 1
        } while (i < this.length && destination != -1)

        return tilted.toString()
    }

    private fun List<String>.sumOfLoadedRocks() =
        this.mapIndexed { idx, value ->  value.count { it == 'O' } * (this.size - idx) }.sum()
}

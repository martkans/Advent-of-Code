import kotlin.io.path.Path

object Runner {
    private val caves = mutableListOf<Cave>()

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.readFile(Path("calendar/day-12-passage-pathing/src/main/resources/test-0-data.txt"))
        convertLinesToCavesAndTunnels(lines)

        println(caves)
    }

    private fun convertLinesToCavesAndTunnels(lines: List<String>) {
        lines.forEach { line ->
            val splittedLine = line.split("-")

            if (!caves.map { cave -> cave.name }.contains(splittedLine[0])) {
                caves.add(Cave(splittedLine[0], getCaveType(splittedLine[0])))
            }
            if (!caves.map { cave -> cave.name }.contains(splittedLine[1])) {
                caves.add(Cave(splittedLine[1], getCaveType(splittedLine[1])))
            }

            val cave1 = caves.first { it.name == splittedLine[0] }
            val cave2 = caves.first { it.name == splittedLine[1] }

            cave1.tunnels.add(cave2)
            cave2.tunnels.add(cave1)
        }
    }

    private fun getCaveType(caveName: String): CaveType = when (caveName) {
        "start" -> CaveType.START
        "end" -> CaveType.END
        else -> {
            if (caveName[0].isLowerCase()) CaveType.SMALL
            else CaveType.BIG
        }
    }

    private fun stepBack(currentPath: List<String>) = currentPath.dropLast(1)

    
}
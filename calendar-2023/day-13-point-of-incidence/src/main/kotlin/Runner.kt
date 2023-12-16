import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-13-point-of-incidence/src/main/resources/data.txt"))
        val terrains = extractTerrains(data)

        println("Result for I: ${terrains.sumOf { it.findVerticalReflection() + it.findHorizontalReflection()}}")
        println("Result for II: ${terrains.sumOf { it.findHorizontalFixedReflection() + it.findVerticalFixedReflection()}}")
    }

    private fun extractTerrains(data: List<String>): List<Terrain> {
        val terrains = mutableListOf<Terrain>()
        val emptyLinesIndexes = data.withIndex().filter { it.value.isEmpty() }.map { it.index } + data.size
        var starting = 0
        var emptyLineIdx = 0

        do {
            terrains.add(Terrain(data.subList(starting, emptyLinesIndexes[emptyLineIdx])))
            starting = emptyLinesIndexes[emptyLineIdx] + 1
            emptyLineIdx += 1
        } while (starting < data.size)

        return terrains
    }

    @JvmInline
    value class Terrain(val value: List<String>) {
        fun findHorizontalReflection() = findReflection(this.value, 100)

        fun findVerticalReflection() = findReflection(transposed())

        fun findHorizontalFixedReflection() = findReflectionWithFixedPatter(this.value, 100)

        fun findVerticalFixedReflection() = findReflectionWithFixedPatter(transposed())

        private fun transposed(): List<String> {
            val transposed = mutableListOf<String>()
            for (i in value.first().indices) {
                transposed += value.map { it[i] }.toString()
            }
            return transposed
        }

        private fun findReflection(terrain: List<String>, multiplier: Int = 1): Int {
            for (i in (1 until terrain.size)) {
                if (terrain[i] == terrain[i - 1]) {
                    var backward = i - 2
                    var forward = i + 1
                    while (backward >= 0 && forward < terrain.size && terrain[backward] == terrain[forward]) {
                        backward -= 1
                        forward += 1
                    }
                    if (backward == -1 || forward == terrain.size) return multiplier * i
                }
            }
            return 0
        }

        private fun findReflectionWithFixedPatter(terrain: List<String>, multiplier: Int = 1): Int {
            for (i in (1 until terrain.size)) {
                if (terrain[i] == terrain[i - 1] || terrain[i].differsOnlyByOneCharacter(terrain[i-1])) {
                    var oneCharacterDifferenceFound = terrain[i].differsOnlyByOneCharacter(terrain[i-1])
                    var backward = i - 2
                    var forward = i + 1
                    while (backward >= 0 && forward < terrain.size
                        && (terrain[backward] == terrain[forward] || (!oneCharacterDifferenceFound && terrain[backward].differsOnlyByOneCharacter(terrain[forward])))
                    ) {
                        oneCharacterDifferenceFound = if (terrain[backward].differsOnlyByOneCharacter(terrain[forward])) true else oneCharacterDifferenceFound
                        backward -= 1
                        forward += 1
                    }
                    if ((backward == -1 || forward == terrain.size) && oneCharacterDifferenceFound) return multiplier * i
                }
            }
            return 0
        }
    }

    fun String.differsOnlyByOneCharacter(other: String): Boolean {
        var x = 0
        for (i in this.indices) {
            if (this[i] != other[i]) x+=1
            if (x > 1) return false
        }

        return x == 1
    }

}

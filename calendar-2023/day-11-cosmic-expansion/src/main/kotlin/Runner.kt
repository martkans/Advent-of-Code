import java.lang.StringBuilder
import kotlin.io.path.Path
import kotlin.math.abs

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val image = Utils.readFile(Path("calendar-2023/day-11-cosmic-expansion/src/main/resources/data.txt"))
        val expandedImage = image.expandSpaces()
        val galaxiesPositions = expandedImage.findAllGalaxies()
        println("Result for I: ${galaxiesPositions.getSumOfAllShortestPathsBetweenGalaxies()}")

        val positions = image.getPositionsOfExpandingSpaces()
        val galaxiesPositionsInNotExpandedImage = image.findAllGalaxies()

        println("Result for II: ${galaxiesPositionsInNotExpandedImage.getSumOfAllShortestPathsBetweenGalaxies(positions)}")

    }

    private fun List<String>.expandSpaces(): List<String> {
        val expandedSpace = mutableListOf<StringBuilder>()
        this.forEach {
            if (it.all { point -> point == '.' }) {
                expandedSpace.add(StringBuilder(it))
            }
            expandedSpace.add(StringBuilder(it))
        }

        val columnsToExpand = mutableListOf<Int>()

        for (i in 0 until this.first().length) {
            if (this.map { it[i] }.all { point -> point == '.' }) {
                columnsToExpand.add(i)
            }
        }

        columnsToExpand.forEachIndexed { num, idx ->
            for (i in expandedSpace.indices) {
                expandedSpace[i].insert(num + idx, '.')
            }
        }

        return expandedSpace.map { it.toString() }
    }

    private fun List<String>.findAllGalaxies(): List<Pair<Int, Int>> {
        val galaxies = mutableListOf<Pair<Int, Int>>()
        this.indices.forEach { i ->
            this[i].indices.forEach { j ->
                if (this[i][j] == '#') {
                    galaxies.add(i to j)
                }
            }
        }
        return galaxies
    }

    private fun List<Pair<Int, Int>>.getSumOfAllShortestPathsBetweenGalaxies(positionsOfExpandedSpaces: List<Pair<ExpandingDirection, Int>>? = null): Long {
        var sum = 0L
        val columnsExpanded = positionsOfExpandedSpaces?.let { it.filter { it.first == ExpandingDirection.COLUMN }.map { it.second } }
        val rowsExpanded = positionsOfExpandedSpaces?.let { it.filter { it.first == ExpandingDirection.ROW }.map { it.second } }

        this.forEachIndexed { idx, galaxy ->
            for (i in idx until this.size) {
                val rowModifier = rowsExpanded?.let {
                    (minOf(galaxy.first, this[i].first)..maxOf(galaxy.first, this[i].first)).intersect(it).size * 999_999
                } ?: 0

                val columnModifier = columnsExpanded?.let {
                    (minOf(galaxy.second, this[i].second)..maxOf(galaxy.second, this[i].second)).intersect(it).size * 999_999
                } ?: 0

                sum += abs(galaxy.first - this[i].first) + rowModifier + abs(galaxy.second - this[i].second) + columnModifier
            }
        }

        return sum
    }

    enum class ExpandingDirection {
        ROW,
        COLUMN
    }

    private fun List<String>.getPositionsOfExpandingSpaces(): List<Pair<ExpandingDirection, Int>> {
        val positionsOfExpandedSpace = mutableListOf<Pair<ExpandingDirection, Int>>()
        this.forEachIndexed { idx, row ->
            if (row.all { point -> point == '.' }) {
                positionsOfExpandedSpace.add(ExpandingDirection.ROW to idx)
            }
        }


        for (i in 0 until this.first().length) {
            if (this.map { it[i] }.all { point -> point == '.' }) {
                positionsOfExpandedSpace.add(ExpandingDirection.COLUMN to i)
            }
        }
        return positionsOfExpandedSpace
    }

}

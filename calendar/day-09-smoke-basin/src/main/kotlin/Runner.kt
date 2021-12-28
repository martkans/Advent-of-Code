import Utils.splitLineBy
import kotlin.io.path.Path
import kotlin.math.abs

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val heightMap = Utils.readFile(Path("calendar/day-09-smoke-basin/src/main/resources/data.txt"))
            .map { row -> row.split("").filter { it.isNotEmpty() }.map { it.toInt() }.toMutableList() }
            .toMutableList()

        val lowPoints = findLowPoints(heightMap)
        println("Sum of risk levels of low points is: ${calculateSumOfRiskLevels(lowPoints)}")
    }

    private fun findLowPoints(heightMap: MutableList<MutableList<Int>>): List<Int> {
        addBorderToHeightMap(heightMap)

        return (1..heightMap.size - 2).map { x ->
            heightMap[x].filterIndexed{ y, value -> value < 10 && isLowPoint(x, y, heightMap)}
        }.flatten()
    }

    private fun addBorderToHeightMap(heightMap: MutableList<MutableList<Int>>) {
        heightMap.forEach { it.add(0, 10) }
        heightMap.forEach { it.add(10) }
        heightMap.add(0, MutableList(heightMap[0].size) {10})
        heightMap.add(MutableList(heightMap[0].size) {10})
    }

    private fun isLowPoint(x: Int, y: Int, heightMap: List<List<Int>>): Boolean =
        heightMap[x][y] < heightMap[x][y-1] && heightMap[x][y] < heightMap[x][y+1]
                && heightMap[x][y] < heightMap[x-1][y] && heightMap[x][y] < heightMap[x+1][y]

    private fun calculateSumOfRiskLevels(lowPoints: List<Int>): Int = lowPoints.sum() + lowPoints.size
}
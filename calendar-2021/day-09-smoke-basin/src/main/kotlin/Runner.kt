import models.LowPoint
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val heightMap = Utils.readFile(Path("calendar-2021/day-09-smoke-basin/src/main/resources/data.txt"))
            .map { row -> row.split("").filter { it.isNotEmpty() }.map { it.toInt() }.toMutableList() }
            .toMutableList()

        addBorderToHeightMap(heightMap)

        val lowPoints = findLowPoints(heightMap)
        println("Sum of risk levels of low points is: ${calculateSumOfRiskLevels(lowPoints)}")

        println("Product of size of three largest basins: ${getProductOfNBiggestBasinsSizes(3, lowPoints, heightMap)}")
    }

    private fun addBorderToHeightMap(heightMap: MutableList<MutableList<Int>>) {
        heightMap.forEach { it.add(0, 10) }
        heightMap.forEach { it.add(10) }
        heightMap.add(0, MutableList(heightMap[0].size) {10})
        heightMap.add(MutableList(heightMap[0].size) {10})
    }

    private fun findLowPoints(heightMap: MutableList<MutableList<Int>>): List<LowPoint> = (1..heightMap.size - 2)
        .map { x ->
            heightMap[x]
                .mapIndexed { y, value -> LowPoint(x, y, value) }
                .filter{ it.value < 10 && isLowPoint(x, it.y, heightMap)}
        }.flatten()

    private fun isLowPoint(x: Int, y: Int, heightMap: List<List<Int>>): Boolean =
        heightMap[x][y] < heightMap[x][y-1] && heightMap[x][y] < heightMap[x][y+1]
                && heightMap[x][y] < heightMap[x-1][y] && heightMap[x][y] < heightMap[x+1][y]

    private fun calculateSumOfRiskLevels(lowPoints: List<LowPoint>): Int = lowPoints.sumOf { it.value } + lowPoints.size

    private fun getProductOfNBiggestBasinsSizes(n: Int, lowPoints: List<LowPoint>, heightMap: List<List<Int>>): Int =
        findAllBasinsSizes(lowPoints, heightMap).sortedDescending().subList(0, n).fold(1){acc, i -> acc * i }

    private fun findAllBasinsSizes(lowPoints: List<LowPoint>, heightMap: List<List<Int>>): List<Int> =
        lowPoints.map { calculateBasinSizeAround(it, heightMap) }

    private fun calculateBasinSizeAround(lowPoint: LowPoint, heightMap: List<List<Int>>): Int {
        val queue = mutableListOf(lowPoint)
        var basinSize = 1
        var idx = 0

        while (idx < queue.size) {
            val newPoints = findPointsToVisit(queue[idx++], heightMap).filter { !queue.contains(it) }
            basinSize += newPoints.size
            queue.addAll(newPoints)
        }

        return basinSize
    }

    private fun findPointsToVisit(point: LowPoint, heightMap: List<List<Int>>): List<LowPoint> {
        val points = mutableListOf<LowPoint>()
        if(heightMap[point.x-1][point.y] < 9) points.add(LowPoint(point.x-1, point.y, heightMap[point.x-1][point.y]))
        if(heightMap[point.x+1][point.y] < 9) points.add(LowPoint(point.x+1, point.y, heightMap[point.x+1][point.y]))
        if(heightMap[point.x][point.y-1] < 9) points.add(LowPoint(point.x, point.y-1, heightMap[point.x][point.y-1]))
        if(heightMap[point.x][point.y+1] < 9) points.add(LowPoint(point.x, point.y+1, heightMap[point.x][point.y+1]))
        return points
    }
}
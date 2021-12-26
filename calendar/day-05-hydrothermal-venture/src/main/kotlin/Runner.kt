import Utils.splitLineBy
import models.Line
import models.OverlappingPoint
import models.Point
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.readFile(Path("calendar/day-05-hydrothermal-venture/src/main/resources/data.txt"))
            .splitLineBy(" -> ")
            .map {
                val s1 = it[0].split(',')
                val s2 = it[1].split(',')
                Line(Point(s1[0].toInt(), s1[1].toInt()), Point(s2[0].toInt(), s2[1].toInt()))
            }
        val overlappedPoints = getOverlappedPointsBy(lines, false)
        val pointsOverlappedMoreThanTwice = countPointsOverlappedMoreOrEqualThan(2, overlappedPoints)
        println("Points do at least two lines overlap $pointsOverlappedMoreThanTwice")

        val overlappedPointsWithDiagonals = getOverlappedPointsBy(lines, true)
        val pointsOverlappedMoreThanTwiceWithDiagonals = countPointsOverlappedMoreOrEqualThan(2, overlappedPointsWithDiagonals)
        println("Points do at least two lines overlap with diagonals $pointsOverlappedMoreThanTwiceWithDiagonals")
    }

    private fun getOverlappedPointsBy(lines: List<Line>, countDiagonals: Boolean): List<OverlappingPoint> {
        val points = mutableListOf<Point>()
        lines.forEach { points.addAll(drawLine(it, countDiagonals)) }
        val grouped = points.groupBy { it }
        return grouped.keys.map { OverlappingPoint(it.x, it.y, grouped[it]!!.size) }
    }

    private fun drawLine(line: Line, countDiagonals: Boolean): List<Point> = if (line.isVertical) {
        drawVerticalLine(line)
    } else if (line.isHorizontal) {
        drawHorizontalLine(line)
    } else if (countDiagonals) {
        drawDiagonalLine(line)
    } else emptyList()

    private fun drawHorizontalLine(line: Line): List<Point> =
        if (line.p1.x < line.p2.x) (line.p1.x..line.p2.x).map { Point(it, line.p1.y) }
        else (line.p2.x..line.p1.x).map { Point(it, line.p1.y) }

    private fun drawVerticalLine(line: Line): List<Point> =
        if(line.p1.y < line.p2.y) (line.p1.y..line.p2.y).map { Point(line.p1.x, it) }
        else (line.p2.y..line.p1.y).map { Point(line.p1.x, it) }

    private fun drawDiagonalLine(line: Line): List<Point> {
        val xInc: Int
        val yInc: Int
        if (line.p1.x < line.p2.x) {
            if(line.p1.y < line.p2.y) {
                xInc = 1
                yInc = 1
            } else {
                xInc = 1
                yInc = -1
            }
        } else {
            if(line.p1.y < line.p2.y) {
                xInc = -1
                yInc = 1
            } else {
                xInc = -1
                yInc = -1
            }
        }
        val points = mutableListOf(line.p1)
        var x = line.p1.x
        var y = line.p1.y

        do {
            x += xInc
            y += yInc
            points.add(Point(x, y))
        } while (x != line.p2.x && y != line.p2.y)

        return points
    }



    private fun countPointsOverlappedMoreOrEqualThan(times: Int, points: List<OverlappingPoint>): Int
        = points.count { it.overlapping >= times }
}
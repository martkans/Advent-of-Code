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
        val overlappedPoints = getOverlappedPointsBy(lines)
        val pointsOverlappedMoreThanTwice = countPointsOverlappedMoreOrEqualThan(2, overlappedPoints)
        println("Points do at least two lines overlap $pointsOverlappedMoreThanTwice")
    }

    private fun getOverlappedPointsBy(lines: List<Line>): List<OverlappingPoint> {
        val overlappingPoints = mutableListOf<OverlappingPoint>()
        lines.forEach {
            val pointsInLine = if (it.isHorizontal || it.isVertical) drawLine(it) else emptyList()
            pointsInLine.forEach { point ->
                val foundPoint = overlappingPoints.find { d -> d == point }
                if(foundPoint != null) foundPoint.overlapping++
                else overlappingPoints.add(OverlappingPoint(point.x, point.y, 1))
            }
        }
        return overlappingPoints
    }

    private fun drawLine(line: Line): List<Point> = if (line.isVertical) {
        drawVerticalLine(line)
    } else if (line.isHorizontal) {
        drawHorizontalLine(line)
    } else {
        emptyList()
    }

    private fun drawHorizontalLine(line: Line): List<Point> =
        if (line.p1.x < line.p2.x) (line.p1.x..line.p2.x).map { Point(it, line.p1.y) }
        else (line.p2.x..line.p1.x).map { Point(it, line.p1.y) }

    private fun drawVerticalLine(line: Line): List<Point> =
        if(line.p1.y < line.p2.y) (line.p1.y..line.p2.y).map { Point(line.p1.x, it) }
        else (line.p2.y..line.p1.y).map { Point(line.p1.x, it) }

    private fun countPointsOverlappedMoreOrEqualThan(times: Int, points: List<OverlappingPoint>): Int
        = points.count { it.overlapping >= times }
}
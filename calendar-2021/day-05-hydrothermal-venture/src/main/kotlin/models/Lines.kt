package models

open class Point(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean =
        other is Point
                && this.x == other.x
                && this.y == other.y

    override fun hashCode(): Int = this.x.hashCode() + this.y.hashCode()
}

class OverlappingPoint(x: Int, y: Int, var overlapping: Int = 0): Point(x, y)

data class Line(val p1: Point, val p2: Point) {
    val isHorizontal: Boolean
        get() = p1.y == p2.y

    val isVertical: Boolean
        get() = p1.x == p2.x
}

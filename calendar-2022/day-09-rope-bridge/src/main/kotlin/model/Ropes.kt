package model


data class Rope(
    val headPosition: Pair<Int, Int> = Pair(0, 0),
    val tailPosition: Pair<Int, Int> = Pair(0, 0)
)

data class RopeWithMultipleKnots(
    val knots: MutableList<Pair<Int, Int>> = (0..9).map { Pair(0, 0) }.toMutableList()
)

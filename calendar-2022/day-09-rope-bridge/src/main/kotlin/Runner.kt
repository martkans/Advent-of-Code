import Utils.splitLineBy
import model.*
import kotlin.io.path.Path
import kotlin.math.abs

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val steps =
            Utils.readFile(Path("calendar-2022/day-09-rope-bridge/src/main/resources/test-data.txt"))
                .splitLineBy(" ").map { Step(Direction.values().first { dir -> dir.letter == it[0] }, it[1].toInt()) }

        val visitedPoints = performRopeWalk(steps)
//        val visitedPointsByComplexRope = performComplexRopeWalk(steps)

        println("Result for part I: ${visitedPoints.size}")
//        println("Result for part II: ${visitedPointsByComplexRope.size}")
    }


    private fun performRopeWalk(steps: List<Step>): Set<Pair<Int, Int>> {
        var currentRopePosition = Rope()
        val visitedPositionsByTail = mutableSetOf<Pair<Int, Int>>()
        steps.forEach { step ->
            val visitedPositionsInStep = (1..step.repeat).map { _ ->
                currentRopePosition = performStep(step.direction, currentRopePosition)
                currentRopePosition.copy()
            }
            visitedPositionsByTail.addAll(visitedPositionsInStep.map { it.tailPosition })
            currentRopePosition = visitedPositionsInStep.last()
        }

        return visitedPositionsByTail
    }

    private fun performStep(direction: Direction, rope: Rope): Rope =
        when (direction) {
            Direction.UP -> performStepUp(rope)
            Direction.DOWN -> performStepDown(rope)
            Direction.LEFT -> performStepLeft(rope)
            Direction.RIGHT -> performStepRight(rope)
        }


    private fun performStepRight(rope: Rope) =
        with(Pair(rope.headPosition.first + 1, rope.headPosition.second)) {
            if (isNewHeadIsFarFromTail(this, rope.tailPosition))
                Rope(this, rope.headPosition)
            else Rope(this, rope.tailPosition)
        }

    private fun performStepLeft(rope: Rope) =
        with(Pair(rope.headPosition.first - 1, rope.headPosition.second)) {
            if (isNewHeadIsFarFromTail(this, rope.tailPosition))
                Rope(this, rope.headPosition)
            else Rope(this, rope.tailPosition)
        }

    private fun performStepUp(rope: Rope) =
        with(Pair(rope.headPosition.first, rope.headPosition.second + 1)) {
            if (isNewHeadIsFarFromTail(this, rope.tailPosition))
                Rope(this, rope.headPosition)
            else Rope(this, rope.tailPosition)
        }

    private fun performStepDown(rope: Rope) =
        with(Pair(rope.headPosition.first, rope.headPosition.second - 1)) {
            if (isNewHeadIsFarFromTail(this, rope.tailPosition))
                Rope(this, rope.headPosition)
            else Rope(this, rope.tailPosition)
        }


    private fun performComplexRopeWalk(steps: List<Step>): Set<Pair<Int, Int>> {
        var currentRopePosition = RopeWithMultipleKnots()
        val visitedPositionsByTail = mutableSetOf<Pair<Int, Int>>()
        steps.forEach { step ->
            val visitedPositionsInStep = (1..step.repeat).map { _ ->
                currentRopePosition = performStep(step.direction, currentRopePosition)
                currentRopePosition.knots.forEachIndexed {index, pair -> print("$index: $pair\t") }
                println()
                currentRopePosition.copy()
            }
            visitedPositionsByTail.addAll(visitedPositionsInStep.map { it.knots.last() })
            currentRopePosition = visitedPositionsInStep.last()
        }

        return visitedPositionsByTail
    }

    private fun performStep(direction: Direction, rope: RopeWithMultipleKnots): RopeWithMultipleKnots =
        when (direction) {
            Direction.UP -> performStepUp(rope)
            Direction.DOWN -> performStepDown(rope)
            Direction.LEFT -> performStepLeft(rope)
            Direction.RIGHT -> performStepRight(rope)
        }


    private fun performStepRight(rope: RopeWithMultipleKnots) =
        mutableListOf(
            Pair(rope.knots[0].first + 1, rope.knots[0].second)
        ).let { newKnots ->
            (0 until rope.knots.size - 1).forEach {
                if (isNewHeadIsFarFromTail(newKnots[it], rope.knots[it + 1]))
                    newKnots.add(Pair(rope.knots[it + 1].first + 1, rope.knots[it + 1].second))
                else newKnots.add(rope.knots[it + 1])
            }
            RopeWithMultipleKnots(newKnots)
        }


    private fun performStepLeft(rope: RopeWithMultipleKnots) =
        mutableListOf(
            Pair(rope.knots[0].first - 1, rope.knots[0].second)
        ).let { newKnots ->
            (0 until rope.knots.size - 1).forEach {
                if (isNewHeadIsFarFromTail(newKnots[it], rope.knots[it + 1]))
                    newKnots.add(Pair(rope.knots[it + 1].first - 1, rope.knots[it + 1].second))
                else newKnots.add(rope.knots[it + 1])
            }
            RopeWithMultipleKnots(newKnots)
        }

    private fun performStepUp(rope: RopeWithMultipleKnots) =
        mutableListOf(
            Pair(rope.knots[0].first, rope.knots[0].second + 1)
        ).let { newKnots ->
            (0 until rope.knots.size - 1).forEach {
                if (isNewHeadIsFarFromTail(newKnots[it], rope.knots[it + 1]))
                    newKnots.add(Pair(rope.knots[it + 1].first, rope.knots[it + 1].second + 1))
                else newKnots.add(rope.knots[it + 1])
            }
            RopeWithMultipleKnots(newKnots)
        }

    private fun performStepDown(rope: RopeWithMultipleKnots) =
        mutableListOf(
            Pair(rope.knots[0].first, rope.knots[0].second - 1)
        ).let { newKnots ->
            (0 until rope.knots.size - 1).forEach {
                if (isNewHeadIsFarFromTail(newKnots[it], rope.knots[it + 1]))
                    newKnots.add(Pair(rope.knots[it + 1].first, rope.knots[it + 1].second - 1))
                else newKnots.add(rope.knots[it + 1])
            }
            RopeWithMultipleKnots(newKnots)
        }


    private fun isNewHeadIsFarFromTail(newHead: Pair<Int, Int>, tail: Pair<Int, Int>) =
        abs(newHead.first - tail.first) > 1 || abs(newHead.second - tail.second) > 1
}
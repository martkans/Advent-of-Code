import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val floor = Utils.readFile(Path("calendar-2023/day-16-the-floor-will-be-lava/src/main/resources/data.txt"))
        floor.startBeam()
        println("Result for I: ${countEnergizedTiles()}")
        println("Result for II: ${floor.getMaxEnergizedTiles()}")
    }
}

val steps: MutableList<Step> = mutableListOf()

fun List<String>.startBeam(initStep: Step = Step()) {
    steps.clear()
    steps.add(initStep)

    var i = 0
    while (i < steps.size) {
        when (steps[i].direction) {
            Direction.RIGHT -> when (this[steps[i].y][steps[i].x]) {
                '/' -> this.validateAndAddStep(Step(steps[i].y - 1, steps[i].x, Direction.UP))
                '\\' -> this.validateAndAddStep(Step(steps[i].y + 1, steps[i].x, Direction.DOWN))
                '|' -> {
                    this.validateAndAddStep(Step(steps[i].y - 1, steps[i].x, Direction.UP))
                    this.validateAndAddStep(Step(steps[i].y + 1, steps[i].x, Direction.DOWN))
                }

                else -> this.validateAndAddStep(Step(steps[i].y, steps[i].x + 1, Direction.RIGHT))
            }

            Direction.LEFT -> when (this[steps[i].y][steps[i].x]) {
                '/' -> this.validateAndAddStep(Step(steps[i].y + 1, steps[i].x, Direction.DOWN))
                '\\' -> this.validateAndAddStep(Step(steps[i].y - 1, steps[i].x, Direction.UP))
                '|' -> {
                    this.validateAndAddStep(Step(steps[i].y - 1, steps[i].x, Direction.UP))
                    this.validateAndAddStep(Step(steps[i].y + 1, steps[i].x, Direction.DOWN))
                }

                else -> this.validateAndAddStep(Step(steps[i].y, steps[i].x - 1, Direction.LEFT))
            }

            Direction.UP -> when (this[steps[i].y][steps[i].x]) {
                '/' -> this.validateAndAddStep(Step(steps[i].y, steps[i].x + 1, Direction.RIGHT))
                '\\' -> this.validateAndAddStep(Step(steps[i].y, steps[i].x - 1, Direction.LEFT))
                '-' -> {
                    this.validateAndAddStep(Step(steps[i].y, steps[i].x + 1, Direction.RIGHT))
                    this.validateAndAddStep(Step(steps[i].y, steps[i].x - 1, Direction.LEFT))
                }

                else -> this.validateAndAddStep(Step(steps[i].y - 1, steps[i].x, Direction.UP))
            }

            Direction.DOWN -> when (this[steps[i].y][steps[i].x]) {
                '/' -> this.validateAndAddStep(Step(steps[i].y, steps[i].x - 1, Direction.LEFT))
                '\\' -> this.validateAndAddStep(Step(steps[i].y, steps[i].x + 1, Direction.RIGHT))
                '-' -> {
                    this.validateAndAddStep(Step(steps[i].y, steps[i].x + 1, Direction.RIGHT))
                    this.validateAndAddStep(Step(steps[i].y, steps[i].x - 1, Direction.LEFT))
                }

                else -> this.validateAndAddStep(Step(steps[i].y + 1, steps[i].x, Direction.DOWN))
            }
        }
        i += 1
    }
}

fun List<String>.isPointOnFloor(y: Int, x: Int) = y >= 0 && y < this.size && x >= 0 && x < this.first().length

fun List<String>.validateAndAddStep(step: Step) {
    if (this.isPointOnFloor(step.y, step.x) && !steps.contains(step)) {
        steps.add(step)
    }
}

fun countEnergizedTiles() = steps.map { it.x to it.y }.distinct().size

fun List<String>.getMaxEnergizedTiles(): Int {
    val maxA = indices.maxOf {
        this.startBeam(Step(it, 0, Direction.RIGHT))
        val right = countEnergizedTiles()
        this.startBeam(Step(it, this.first().length - 1, Direction.LEFT))
        val left = countEnergizedTiles()

        maxOf(right, left)
    }

    val maxB = (0 until this.first().length).maxOf {
        this.startBeam(Step(0, it, Direction.DOWN))
        val down = countEnergizedTiles()
        this.startBeam(Step(this.size - 1, it, Direction.UP))
        val up = countEnergizedTiles()

        maxOf(down, up)
    }

    return maxOf(maxA, maxB)
}

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

data class Step(
    val y: Int = 0,
    val x: Int = 0,
    val direction: Direction = Direction.RIGHT
)

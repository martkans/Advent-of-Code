package model

data class Step(val direction: Direction, val repeat: Int)

enum class Direction(val letter: String) {
    UP("U"), DOWN("D"), LEFT("L"), RIGHT("R");
}

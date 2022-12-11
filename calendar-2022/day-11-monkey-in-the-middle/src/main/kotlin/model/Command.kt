package model

sealed class Command(
    val cycles: Int
)

data class Addx(val value: Int): Command(2)

class Noop(): Command(1)

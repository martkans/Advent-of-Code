package models

data class Board(val cells: List<List<Cell>>)

data class Cell(val value: Int, var marked: Boolean = false)

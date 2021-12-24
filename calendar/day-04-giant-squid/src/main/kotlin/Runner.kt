import models.Board
import models.Cell
import kotlin.io.path.Path

object Runner {

    private const val BINGO_SIZE = 5

    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar/day-04-giant-squid/src/main/resources/data.txt"))
        val numbers = data[0].split(",").map { it.toInt() }
        val boards = createBoards(data)

        val score = playBingo(boards, numbers)
        println("Score: $score")

        val losingScore = playBingoToLose(boards, numbers)
        println("Losing score: $losingScore")
    }

    private fun createBoards(data: List<String>): List<Board> {
        val processedRows = data.filter { it != "" }.drop(1)
            .map { sp -> sp.split(" ").filter { it != "" }.map { Cell(it.toInt()) } }

        val boards: MutableList<Board> = mutableListOf()

        var idx = 0
        while (idx + BINGO_SIZE <= processedRows.size) {
            boards += Board(processedRows.subList(idx, idx + BINGO_SIZE))
            idx += BINGO_SIZE
        }
        return boards
    }

    private fun playBingo(boards: List<Board>, numbers: List<Int>): Int? {
        numbers.forEach { number ->
            boards.forEach { board ->
                val cord = markValue(board, number)
                if (cord != null) {
                    val isWin = checkIfWin(board, cord.first, cord.second)
                    if (isWin) {
                        return calculateScore(board, number)
                    }
                }
            }
        }
        return null
    }

    private fun playBingoToLose(boards: List<Board>, numbers: List<Int>): Int? {
        val ignoredBoardsIdx = mutableListOf<Int>()
        numbers.forEach { number ->
            boards.forEachIndexed { index, board ->
                if (!ignoredBoardsIdx.contains(index)) {
                    val cord = markValue(board, number)
                    if (cord != null) {
                        val isWin = checkIfWin(board, cord.first, cord.second)
                        if (isWin) {
                            if (ignoredBoardsIdx.size == boards.size - 1) {
                                return calculateScore(board, number)
                            } else {
                                ignoredBoardsIdx += index
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    private fun markValue(board: Board, value: Int): Pair<Int, Int>? {
        board.cells.forEachIndexed { indexX, list ->
            list.forEachIndexed { indexY, cell ->
                if (cell.value == value) {
                    cell.marked = true
                    return Pair(indexX, indexY)
                }
            }
        }
        return null
    }

    private fun checkIfWin(board: Board, x: Int, y: Int): Boolean =
        board.cells[x].filter { it.marked }.size == BINGO_SIZE
                || board.cells.filter { it[y].marked }.size == BINGO_SIZE

    private fun calculateScore(board: Board, value: Int): Int =
        board.cells.sumOf { it.filter { cell -> !cell.marked }.sumOf { cell -> cell.value } } * value
}
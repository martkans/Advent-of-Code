import kotlin.io.path.Path

object Runner {
    private val illegalCharactersScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    private val openingCharactersToClosingCharacters = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )

    @JvmStatic
    fun main(args: Array<String>) {
        val subsystemLines = Utils.readFile(Path("calendar/day-10-syntax-scoring/src/main/resources/data.txt"))
        
        println("Total score of corrupted lines: ${calculateScoreOfCorruptedLines(subsystemLines)}")
    }
    
    private fun calculateScoreOfCorruptedLines(subsystemLines: List<String>) =
        subsystemLines.sumOf { calculateScoreOfSingleCorruptedLine(it) }

    private fun calculateScoreOfSingleCorruptedLine(subsystemLine: String): Int {
        val stack = mutableListOf<Char>()
        subsystemLine.forEach {
            if (openingCharactersToClosingCharacters.containsKey(it)) stack.add(it)
            else if (openingCharactersToClosingCharacters[stack.last()] == it) stack.removeLast()
            else return illegalCharactersScores[it]!!
        }
        return 0
    }
}
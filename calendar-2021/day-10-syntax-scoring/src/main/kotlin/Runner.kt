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

    private val incompleteCharactersScores = mapOf(
        '(' to 1,
        '[' to 2,
        '{' to 3,
        '<' to 4
    )

    private val closingCharactersToOpeningCharacters = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )

    @JvmStatic
    fun main(args: Array<String>) {
        val subsystemLines = Utils.readFile(Path("calendar-2021/day-10-syntax-scoring/src/main/resources/data.txt"))
        
        println("Total score of corrupted lines: ${calculateScoreOfCorruptedLines(subsystemLines)}")
        println("Middle score of incomplete lines: ${middleScoreOfIncompleteLines(calculateScoresOfIncompleteLines(subsystemLines))}")
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

    private fun middleScoreOfIncompleteLines(scores: List<Long>) = scores.sorted()[scores.size/2]

    private fun calculateScoresOfIncompleteLines(subsystemLines: List<String>): List<Long> =
        subsystemLines.filter { !isCorruptedLine(it) }.map { calculateScoreOfSingleIncompleteLine(it) }

    private fun calculateScoreOfSingleIncompleteLine(subsystemLine: String): Long {
        val stack = mutableListOf<Char>()
        var score = 0L
        subsystemLine.reversed().forEach {
            if (closingCharactersToOpeningCharacters.containsKey(it)) stack.add(it)
            else if (stack.isNotEmpty() && closingCharactersToOpeningCharacters[stack.last()] == it) stack.removeLast()
            else score = score * 5 + incompleteCharactersScores[it]!!
        }
        return score
    }

    private fun isCorruptedLine(subsystemLine: String): Boolean {
        val stack = mutableListOf<Char>()
        subsystemLine.forEach {
            if (openingCharactersToClosingCharacters.containsKey(it)) stack.add(it)
            else if (openingCharactersToClosingCharacters[stack.last()] == it) stack.removeLast()
            else return true
        }
        return false
    }
}
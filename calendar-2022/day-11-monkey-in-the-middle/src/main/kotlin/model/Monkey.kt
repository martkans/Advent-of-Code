package model

class Monkey(
    val items: MutableList<Long> = mutableListOf(),
    private val operation: (Long) -> Long,
    private val test: (Long) -> Int,
    var inspectingCount: Int = 0
) {

    fun inspectItem(currentWorryLevel: Long): Long {
        inspectingCount++
        return operation.invoke(currentWorryLevel)
    }

    fun monkeyGetBored(currentWorryLevel: Long) = currentWorryLevel/3

    fun throwToAnotherMonkey(currentWorryLevel: Long) = test.invoke(currentWorryLevel)
}


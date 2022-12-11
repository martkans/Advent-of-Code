import model.Monkey

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {

        val testMonkeys = listOf(
            Monkey(
                items = mutableListOf(79, 98),
                operation = { old -> old * 19 },
                test = { worryLevel -> if (worryLevel % 23 == 0L) 2 else 3 },
            ),
            Monkey(
                items = mutableListOf(54, 65, 75, 74),
                operation = { old -> old + 6 },
                test = { worryLevel -> if (worryLevel % 19 == 0L) 2 else 0 },
            ),
            Monkey(
                items = mutableListOf(79, 60, 97),
                operation = { old -> old * old },
                test = { worryLevel -> if (worryLevel % 13 == 0L) 1 else 3 },
            ),
            Monkey(
                items = mutableListOf(74),
                operation = { old -> old + 3 },
                test = { worryLevel -> if (worryLevel % 17 == 0L) 0 else 1 },
            )
        )

        testMonkeys.playRound(20)

        println("Result for part I: ${testMonkeys.getMostActiveMonkeysProduct()}")
    }


    private fun List<Monkey>.playRound(times: Int = 1) {
        (1..times).forEach { _ ->
            for (monkeyIdx in this.indices) {
                for (i in this[monkeyIdx].items.indices) {
                    this[monkeyIdx].items[i] = this[monkeyIdx].inspectItem(this[monkeyIdx].items[i])
                    this[monkeyIdx].items[i] = this[monkeyIdx].monkeyGetBored(this[monkeyIdx].items[i])
                    val index = this[monkeyIdx].throwToAnotherMonkey(this[monkeyIdx].items[i])
                    this[index].items.add(this[monkeyIdx].items[i])
                }
                this[monkeyIdx].items.clear()
            }
        }
    }

    private fun List<Monkey>.getMostActiveMonkeysProduct() = this.map { it.inspectingCount }
        .sortedByDescending { it }
        .take(2).reduce { acc, i -> acc * i }
}
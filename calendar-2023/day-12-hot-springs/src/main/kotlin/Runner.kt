import kotlin.io.path.Path
import kotlin.text.StringBuilder

object Runner {
//    NOT BEST SOLUTION
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-12-hot-springs/src/main/resources/data.txt"))
        val conditionRecords = extractData(data)

        println("Result for I: ${conditionRecords.sumOf { it.countPossibleConfigurations() }}")
    }

    data class ConditionRecord(
        val condition: String,
        val contiguousGroup: List<Int>
    ) {
        private val BROKEN = Regex("#+")

        fun countPossibleConfigurations() = createConfiguration().count { checkIfConfigurationPassRequirements(it) }

        private fun checkIfConfigurationPassRequirements(configuration: String): Boolean =
            BROKEN.findAll(configuration).toList().let {
                it.size == contiguousGroup.size && it.zip(contiguousGroup).all { (it.first.range.last - it.first.range.first + 1)  == it.second }
            }


        private fun createConfiguration(a: String = ""): List<String> {
            var idx = a.length
            val b = StringBuilder(a)
            while (idx < condition.length && condition[idx] != '?') {
                b.append(condition[idx])
                idx += 1
            }
            if (idx == condition.length) return listOf(b.toString())
            return listOf(
                createConfiguration("$b."),
                createConfiguration("$b#")
            ).flatten()
        }

    }

    private fun extractData(data: List<String>) = data.map { line ->
        line.split(" ").let {
            ConditionRecord(
                condition = it.first(),
                contiguousGroup = it.last().split(",").map { it.toInt() }
            )
        }
    }

}

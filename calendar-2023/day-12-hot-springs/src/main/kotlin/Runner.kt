import kotlin.io.path.Path
import kotlin.system.measureTimeMillis
import kotlin.text.StringBuilder

object Runner {
    //    NOT BEST SOLUTION
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-12-hot-springs/src/main/resources/data.txt"))
        val conditionRecords = extractData(data)

        val a = measureTimeMillis {
            println("Result for I: ${conditionRecords.sumOf { it.createAndCountConfigurations() }}")
        }

        val b = measureTimeMillis {
            println("Result for I: ${conditionRecords.sumOf { it.countPossibleConfigurations() }}")
        }

        // nice comparison
        println("Time for optimized method: $a ms")
        println("Time for not optimized method: $b ms")

        val extendedRecords = conditionRecords.extendData()

    }

    data class ConditionRecord(
        val condition: String,
        val contiguousGroup: List<Int>
    ) {
        private val BROKEN = Regex("#+")

        fun countPossibleConfigurations() = createConfiguration().count { checkIfConfigurationPassRequirements(it) }

        private fun checkIfConfigurationPassRequirements(configuration: String): Boolean =
            BROKEN.findAll(configuration).toList().let {
                it.size == contiguousGroup.size && it.zip(contiguousGroup)
                    .all { (it.first.range.last - it.first.range.first + 1) == it.second }
            }

        private fun checkIfConfigurationPassRequirementsPartially(configuration: String): Boolean =
            BROKEN.findAll(configuration).toList().let {
                with(it.zip(contiguousGroup)) {
                    if (this.isEmpty()) {
                        true
                    } else if (this.last().first.range.last - this.last().first.range.first + 1 > this.last().second)
                        false
                    else this.dropLast(1).all { (it.first.range.last - it.first.range.first + 1) == it.second }
                }
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

        fun createAndCountConfigurations(a: String = ""): Int {
            var idx = a.length
            val b = StringBuilder(a)
            while (idx < condition.length && condition[idx] != '?') {
                b.append(condition[idx])
                idx += 1
            }

            if (idx == condition.length) {
                return if (checkIfConfigurationPassRequirements(b.toString())) 1 else 0
            }

            val dotSegment =
                if (checkIfConfigurationPassRequirementsPartially("$b.")) createAndCountConfigurations("$b.") else 0
            val hashSegment =
                if (checkIfConfigurationPassRequirementsPartially("$b#")) createAndCountConfigurations("$b#") else 0
            return dotSegment + hashSegment
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

    private fun List<ConditionRecord>.extendData(multiplier: Int = 5) = this.map { record ->
        ConditionRecord(
            condition = (List(multiplier) { record.condition }).joinToString("?"),
            contiguousGroup = (List(multiplier) { record.contiguousGroup }).flatten()
        )
    }

}

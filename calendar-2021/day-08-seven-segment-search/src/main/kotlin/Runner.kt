import kotlin.io.path.Path
import models.Record
import kotlin.streams.toList

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.readFile(Path("calendar-2021/day-08-seven-segment-search/src/main/resources/data.txt"))

        val records = lines.map {
            val temp = it.split(" | ")
            Record(temp[0].split(' '), temp[1].split(' '))
        }

        println("Number of easy digits: ${countEasyDigits(records)}")

        println("Sum of decoded outputs: ${findSumOfDecodedOutputs(records)}")
    }

    private fun countEasyDigits(records: List<Record>) = records.flatMap { it.outputs }.count {
        val distinctLetters = countDistinctLetters(it)
        distinctLetters == 2 || distinctLetters == 3 || distinctLetters == 4 || distinctLetters == 7
    }

    private fun countDistinctLetters(s: String): Int = s.chars().distinct().count().toInt()

    private fun findSumOfDecodedOutputs(records: List<Record>) =
        records.map { Pair(it, computeDecoder(it.signals)) }.sumOf { decodeOutput(it.first.outputs, it.second) }

    private fun computeDecoder(signals: List<String>): Map<String, Int> {
        val encoder = mutableMapOf<Int, String>()

        encoder[1] = signals.first { it.length == 2 }
        encoder[7] = signals.first { it.length == 3 }
        encoder[4] = signals.first { it.length == 4 }
        encoder[8] = signals.first { it.length == 7 }
        encoder[9] = findNine(signals, encoder[4]!!, encoder[7]!!)

        val fiveAndSix = findFiveAndSix(signals, encoder[9]!!)
        encoder[5] = fiveAndSix!!.first
        encoder[6] = fiveAndSix.second

        encoder[0] = signals.first { it.length == 6 && it != encoder[6] && it != encoder[9] }

        val twoAndThree = findTwoAndThree(signals, encoder[5]!!, encoder[6]!!)
        encoder[2] = twoAndThree.first
        encoder[3] = twoAndThree.second

        return encoder.map { entry ->
            Pair(
                entry.value.chars().sorted().toList().map { it.toChar() }.fold("") { acc, el -> acc + el },
                entry.key
            )
        }.toMap()
    }

    private fun findNine(signals: List<String>, fourEncoded: String, sevenEncoded: String): String {
        val partOfNine = (fourEncoded + sevenEncoded).chars().distinct().toList()
        val sixLettersSignalToInts = signals.filter { it.length == 6 }.map { it.chars().toList() }
        return sixLettersSignalToInts.first { it.containsAll(partOfNine) }
            .map { it.toChar() }.fold("") { acc, el -> acc + el }
    }

    private fun findFiveAndSix(signals: List<String>, nineEncoded: String): Pair<String, String>? {
        val sixLettersSignalToInts = signals.filter { it.length == 6 && it != nineEncoded }.map { it.chars().toList() }
        val fiveLettersSignalToInts = signals.filter { it.length == 5 }.map { it.chars().toList() }

        sixLettersSignalToInts.forEach { six ->
            fiveLettersSignalToInts.forEach { five ->
                val contains = six.containsAll(five)
                if (contains) {
                    return Pair(
                        five.map { it.toChar() }.fold("") { acc, el -> acc + el },
                        six.map { it.toChar() }.fold("") { acc, el -> acc + el }
                    )
                }
            }
        }
        return null
    }

    private fun findTwoAndThree(signals: List<String>, five: String, six: String): Pair<String, String> {
        val missingLetter = six.chars().toList().first { !five.contains(it.toChar()) }
        val fiveLettersSignalToInts = signals.filter { it.length == 5 && it != five}.map { it.chars().toList() }
        val two = fiveLettersSignalToInts.first { it.contains(missingLetter) }
        val three = fiveLettersSignalToInts.first { it != two }
        return Pair(
            two.map { it.toChar() }.fold("") { acc, el -> acc + el },
            three.map { it.toChar() }.fold("") { acc, el -> acc + el }
        )
    }

    private fun decodeOutput(outputs: List<String>, decoder: Map<String, Int>): Int = outputs.map { output ->
            output.chars().sorted().toList().map { it.toChar() }.fold("") { acc, el -> acc + el }
        }.map { decoder[it]!!.toString() }.toList().joinToString("").toInt()
}
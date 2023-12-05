import kotlin.io.path.Path
import kotlin.math.pow
import kotlin.time.measureTime

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2023/day-05-if-you-give-a-seed-a-fertilizer/src/main/resources/data.txt"))
            .filter { it.isNotEmpty() }
        val seeds = extractSeeds(data[0])
        val maps = extractProductionStepsMaps(data.subList(1, data.size))
        val dur = measureTime {
            println("Result for I: ${seeds.plantSeeds(maps).min()}")
        }

        println(dur)

        val dur2 = measureTime {
//            println("Result for I: ${extractSeedsWithRangeAndGetMinAfterPlanting(data[0], maps)}")
        }

        println(dur2)
    }

    private val NUM_REGEX = Regex("\\d+")

    private fun List<Long>.plantSeeds(maps: Map<PART, ProductionStepMaps>): List<Long> {
        var source = PART.seed
        var seedsInProcess = this
        do {
            seedsInProcess = seedsInProcess.map { seed ->
                maps[source]!!.determineDestination(seed)
            }
            source = maps[source]!!.destination
        } while ( source != PART.location)
        return seedsInProcess
    }


    private fun Long.plantSeed(maps: Map<PART, ProductionStepMaps>): Long {
        var source = PART.seed
        var seedInProcess = this
        do {
            seedInProcess = maps[source]!!.determineDestination(seedInProcess)
            source = maps[source]!!.destination
        } while ( source != PART.location)
        return seedInProcess
    }

    private fun extractSeeds(line: String) = NUM_REGEX.findAll(line).map { it.value.toLong() }.toList()
    private fun extractSeedsWithRangeAndGetMinAfterPlanting(line: String, maps: Map<PART, ProductionStepMaps>): Long {
        val ranges = NUM_REGEX.findAll(line).map { it.value.toLong() }.toList()
            .chunked(2)

        ranges.sortedBy { it.first() }
            .forEach { println("${it.first()} - ${it.first() + it.last()}") }

        var min = Long.MAX_VALUE
        println(Long.MAX_VALUE)

        ranges.forEach {
            var seed = it.first()
            while (seed <= it.first() + it.last()) {
                min = minOf(min, seed.plantSeed(maps))
                seed += 1
            }
        }

        return min
    }

    private fun extractProductionStepsMaps(lines: List<String>): Map<PART, ProductionStepMaps> {
        val maps = mutableMapOf<PART, ProductionStepMaps>()
        var lastKey = PART.seed
        lines.forEach {
            if (it[0].isDigit()) {
                NUM_REGEX.findAll(it).map { it.value.toLong() }.toList().let { nums ->
                    maps[lastKey]?.maps?.add(
                        RangeMap(nums[1], nums[0], nums[2])
                    )
                }
            } else {
                val (source, dest) = it.substringBefore(" ").split("-to-").map { PART.valueOf(it) }
                maps[source] = ProductionStepMaps(source, dest)
                lastKey = source
            }
        }
        return maps
    }

    enum class PART {
        seed, soil, fertilizer, water, light, temperature, humidity, location
    }

    data class ProductionStepMaps(
        val source: PART,
        val destination: PART,
        val maps: MutableList<RangeMap> = mutableListOf()
    ) {
        fun determineDestination(source: Long): Long =
            maps.find { it.isInSourceRange(source) }?.calculateDestination(source)
                ?: source
    }

    data class RangeMap(
        val sourceRangeStart: Long,
        val destinationRangeStart: Long,
        val rangeLength: Long
    ) {
        fun isInSourceRange(source: Long) = source >= sourceRangeStart && source < sourceRangeStart.plus(rangeLength)
        fun calculateDestination(source: Long) = destinationRangeStart + (source - sourceRangeStart)
    }
}
import kotlin.io.path.Path
import kotlin.math.max
import kotlin.time.measureTime

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data =
            Utils.readFile(Path("calendar-2023/day-05-if-you-give-a-seed-a-fertilizer/src/main/resources/data.txt"))
                .filter { it.isNotEmpty() }
        val seeds = extractSeeds(data[0])
        val maps = extractProductionStepsMaps(data.subList(1, data.size))
        println("Result for I: ${seeds.plantSeeds(maps).min()}")

        val seedRanges = extractSeedRanges(data[0])
        println("Result for II: ${seedRanges.plantSeedRanges(maps).minOf { it.start }}")

    }

    private val NUM_REGEX = Regex("\\d+")

    private fun extractSeedRanges(line: String): List<SeedRange> =
        NUM_REGEX.findAll(line).map { it.value.toLong() }.toList()
            .chunked(2) { SeedRange(it.first(), it.first() + it.last() - 1) }

    private fun List<SeedRange>.plantSeedRanges(maps: Map<PART, ProductionStepMaps>): List<SeedRange> {
        var seedRanges = this
        var status = PART.seed
        while (status != PART.location) {
            seedRanges = seedRanges.flatMap { it.plantSeeds(maps) }
            status = seedRanges.first().step
        }
        return seedRanges
    }

    private fun List<Long>.plantSeeds(maps: Map<PART, ProductionStepMaps>): List<Long> {
        var source = PART.seed
        var seedsInProcess = this
        do {
            seedsInProcess = seedsInProcess.map { seed ->
                maps[source]!!.determineDestination(seed)
            }
            source = maps[source]!!.destination
        } while (source != PART.location)
        return seedsInProcess
    }

    private fun SeedRange.plantSeeds(maps: Map<PART, ProductionStepMaps>): List<SeedRange> {
        val transformation = maps[this.step]!!
        val ranges = transformation.maps.filter { it.isInRange(this) }
            .map { this.splitSeedRange(it) to it.destinationRangeStart - it.sourceRangeStart }
            .sortedBy { it.first.start }.toMutableList()
        if (ranges.isNotEmpty()) {
            if (ranges.first().first.start > this.start) {
                ranges.add(0, SeedRange(this.start, ranges.first().first.start - 1, this.step) to 0)
            }
            if (ranges.last().first.end < this.end) {
                ranges.add(SeedRange(ranges.last().first.end + 1, this.end, this.step) to 0)
            }
        } else {
            ranges.add(this to 0)
        }

        for (i in (0 until ranges.size - 1)) {
            if (ranges[i + 1].first.start - ranges[i].first.end > 1) {
                ranges.add(SeedRange(ranges[i].first.end + 1, ranges[i + 1].first.start - 1, this.step) to 0)
            }
        }

        ranges.forEach { it.first.transform(it.second, transformation.destination) }
        return ranges.map { it.first }
    }

    private fun extractSeeds(line: String) = NUM_REGEX.findAll(line).map { it.value.toLong() }.toList()

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
        maps.forEach { (t, u) -> u.maps.sortBy { it.sourceRangeStart } }
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
            maps.find { it.isSourceInRange(source) }?.calculateDestination(source) ?: source
    }

    data class RangeMap(
        val sourceRangeStart: Long,
        val destinationRangeStart: Long,
        val rangeLength: Long
    ) {
        fun isSourceInRange(source: Long) = source >= sourceRangeStart && source < sourceRangeStart.plus(rangeLength)
        fun calculateDestination(source: Long) = destinationRangeStart + (source - sourceRangeStart)

        fun isInRange(range: SeedRange) =
            sourceRangeStart <= range.end && sourceRangeStart.plus(rangeLength) > range.start
    }

    data class SeedRange(var start: Long, var end: Long, var step: PART = PART.seed)

    private fun SeedRange.splitSeedRange(range: RangeMap): SeedRange = SeedRange(
        start = maxOf(start, range.sourceRangeStart),
        end = minOf(end, range.sourceRangeStart + range.rangeLength - 1),
        step = this.step
    )


    private fun SeedRange.transform(addVal: Long, step: PART) {
        this.apply {
            this.step = step
            this.start += addVal
            this.end += addVal
        }
    }
}
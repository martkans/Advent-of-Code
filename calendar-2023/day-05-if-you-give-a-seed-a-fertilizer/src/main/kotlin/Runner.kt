import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data =
            Utils.readFile(Path("calendar-2023/day-05-if-you-give-a-seed-a-fertilizer/src/main/resources/data.txt"))
                .filter { it.isNotEmpty() }
        val seeds = extractSeeds(data[0])
        val plantingSteps = extractPlantingSteps(data.subList(1, data.size))
        println("Result for I: ${seeds.plantSeeds(plantingSteps).min()}")

        val seedRanges = extractSeedRanges(data[0])
        println("Result for II: ${seedRanges.plantSeedRanges(plantingSteps).minOf { it.start }}")

    }

    private val NUM_REGEX = Regex("\\d+")

    private fun extractSeedRanges(line: String): List<SeedRange> =
        NUM_REGEX.findAll(line).map { it.value.toLong() }.toList()
            .chunked(2) { SeedRange(it.first(), it.first() + it.last() - 1) }

    private fun List<SeedRange>.plantSeedRanges(maps: Map<Step, ProductionStepMaps>): List<SeedRange> {
        var seedRanges = this
        var status = Step.seed
        while (status != Step.location) {
            seedRanges = seedRanges.flatMap { it.plantSeeds(maps) }
            status = seedRanges.first().step
        }
        return seedRanges
    }

    private fun List<Long>.plantSeeds(maps: Map<Step, ProductionStepMaps>): List<Long> {
        var source = Step.seed
        var seedsInProcess = this
        do {
            seedsInProcess = seedsInProcess.map { seed ->
                maps[source]!!.determineDestination(seed)
            }
            source = maps[source]!!.destination
        } while (source != Step.location)
        return seedsInProcess
    }

    private fun SeedRange.plantSeeds(maps: Map<Step, ProductionStepMaps>): List<SeedRange> {
        val productionStep = maps[this.step]!!

        val rangesWithModifiers = productionStep.maps.filter { it.isInSeedRange(this) }
            .map { this.splitSeedRange(it) to it.modifier }
            .sortedBy { it.first.start }.toMutableList()

        rangesWithModifiers.fillBlanks(this)
        rangesWithModifiers.modifySeedRanges(productionStep.destination)
        return rangesWithModifiers.map { it.first }
    }

    private fun MutableList<Pair<SeedRange, Long>>.fillBlanks(sourceSeedRange: SeedRange) {
        if (this.isNotEmpty()) {
            if (this.first().first.start > sourceSeedRange.start) {
                this.add(0, SeedRange(sourceSeedRange.start, this.first().first.start - 1, sourceSeedRange.step) to 0)
            }
            if (this.last().first.end < sourceSeedRange.end) {
                this.add(SeedRange(this.last().first.end + 1, sourceSeedRange.end, sourceSeedRange.step) to 0)
            }
        } else {
            this.add(sourceSeedRange to 0)
        }

        for (i in (0 until this.size - 1)) {
            if (this[i + 1].first.start - this[i].first.end > 1) {
                this.add(SeedRange(this[i].first.end + 1, this[i + 1].first.start - 1, sourceSeedRange.step) to 0)
            }
        }
    }

    private fun List<Pair<SeedRange, Long>>.modifySeedRanges(nextStep: Step) {
        this.forEach { (range, modifier) -> range.transform(modifier, nextStep) }
    }

    private fun extractSeeds(line: String) = NUM_REGEX.findAll(line).map { it.value.toLong() }.toList()

    private fun extractPlantingSteps(lines: List<String>): Map<Step, ProductionStepMaps> {
        val maps = mutableMapOf<Step, ProductionStepMaps>()
        var lastKey = Step.seed
        lines.forEach {
            if (it[0].isDigit()) {
                NUM_REGEX.findAll(it).map { it.value.toLong() }.toList().let { nums ->
                    maps[lastKey]?.maps?.add(
                        RangeMap(nums[1], nums[0], nums[2])
                    )
                }
            } else {
                val (source, dest) = it.substringBefore(" ").split("-to-").map { Step.valueOf(it) }
                maps[source] = ProductionStepMaps(source, dest)
                lastKey = source
            }
        }
        maps.forEach { (t, u) -> u.maps.sortBy { it.sourceRangeStart } }
        return maps
    }

    enum class Step {
        seed, soil, fertilizer, water, light, temperature, humidity, location
    }

    data class ProductionStepMaps(
        val source: Step,
        val destination: Step,
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
        val modifier = destinationRangeStart - sourceRangeStart

        fun isSourceInRange(source: Long) = source >= sourceRangeStart && source < sourceRangeStart.plus(rangeLength)
        fun calculateDestination(source: Long) = destinationRangeStart + (source - sourceRangeStart)

        fun isInSeedRange(range: SeedRange) =
            sourceRangeStart <= range.end && sourceRangeStart.plus(rangeLength) > range.start
    }

    data class SeedRange(var start: Long, var end: Long, var step: Step = Step.seed)

    private fun SeedRange.splitSeedRange(range: RangeMap): SeedRange = SeedRange(
        start = maxOf(start, range.sourceRangeStart),
        end = minOf(end, range.sourceRangeStart + range.rangeLength - 1),
        step = this.step
    )


    private fun SeedRange.transform(addVal: Long, step: Step) {
        this.apply {
            this.step = step
            this.start += addVal
            this.end += addVal
        }
    }
}
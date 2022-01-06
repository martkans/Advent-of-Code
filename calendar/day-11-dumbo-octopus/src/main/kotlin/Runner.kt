import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val dumboOctopuses = Utils.readFile(Path("calendar/day-11-dumbo-octopus/src/main/resources/data.txt"))
            .map { row -> row.split("").filter { it.isNotEmpty() }.map { it.toInt() }.toMutableList() }
            .toMutableList()

        println("Total octopus flashes after 100 steps: ${processNStepsOfOctopusLife(100, dumboOctopuses)}")
    }

    private fun processNStepsOfOctopusLife(n: Int, dumboOctopuses: MutableList<MutableList<Int>>): Int =
        (1..n).sumOf { processStep(dumboOctopuses) }

    private fun processStep(dumboOctopuses: MutableList<MutableList<Int>>): Int {
        var octopusesFlashingInThisStepCounter = 0
        increaseEnergyLevel(dumboOctopuses)
        val copyOfDumboOctopuses = MutableList(dumboOctopuses.size) { out -> MutableList(dumboOctopuses[0].size) { dumboOctopuses[out][it] } }
        while (isAnyOctopusGoingToFlash(dumboOctopuses)) {
            gatherAllEnergyFromOctopusesGoingToFlash(dumboOctopuses, copyOfDumboOctopuses)
            octopusesFlashingInThisStepCounter += flashEnergy(dumboOctopuses)
            mergeOctopusesData(dumboOctopuses, copyOfDumboOctopuses)
        }

        return octopusesFlashingInThisStepCounter
    }

    private fun increaseEnergyLevel(dumboOctopuses: MutableList<MutableList<Int>>) = dumboOctopuses.indices
        .forEach { x -> dumboOctopuses[x].indices.forEach { y ->  dumboOctopuses[x][y]++ } }

    private fun isAnyOctopusGoingToFlash(dumboOctopuses: List<List<Int>>) =
        dumboOctopuses.sumOf { row -> row.count { it > 9 } } > 0

    private fun gatherAllEnergyFromOctopusesGoingToFlash(
        dumboOctopuses: MutableList<MutableList<Int>>,
        copyOfDumboOctopuses: MutableList<MutableList<Int>>
    ) = dumboOctopuses.indices.forEach { x ->
            dumboOctopuses[x].indices.forEach { y ->
                gatherEnergyFromOctopusesGoingToFlash(x, y, dumboOctopuses, copyOfDumboOctopuses)
            }
        }

    private fun gatherEnergyFromOctopusesGoingToFlash(
        x: Int,
        y: Int,
        dumboOctopuses: MutableList<MutableList<Int>>,
        copyOfDumboOctopuses: MutableList<MutableList<Int>>
    ) {
        var counter = 0
        if(dumboOctopuses[x][y] in 1..9) {
            if(x+1 < dumboOctopuses.size && dumboOctopuses[x+1][y] > 9) counter++
            if(y+1 < dumboOctopuses[0].size && dumboOctopuses[x][y+1] > 9) counter++
            if(x-1 >= 0 && dumboOctopuses[x-1][y] > 9) counter++
            if(y-1 >= 0 && dumboOctopuses[x][y-1] > 9) counter++
            if(x+1 < dumboOctopuses.size && y+1 < dumboOctopuses[0].size && dumboOctopuses[x+1][y+1] > 9) counter++
            if(x-1 >= 0 && y-1 >= 0 && dumboOctopuses[x-1][y-1] > 9) counter++
            if(x-1 >= 0 && y+1 < dumboOctopuses[0].size && dumboOctopuses[x-1][y+1] > 9) counter++
            if(y-1 >= 0 && x+1 < dumboOctopuses.size && dumboOctopuses[x+1][y-1] > 9) counter++
            copyOfDumboOctopuses[x][y] += counter
        }
    }

    private fun flashEnergy(dumboOctopuses: MutableList<MutableList<Int>>): Int {
        var counter = 0
        dumboOctopuses.indices.forEach { x ->
            dumboOctopuses[x].indices.forEach { y ->
                if(dumboOctopuses[x][y] > 9) {
                    dumboOctopuses[x][y] = 0
                    counter++
                }
            }
        }
        return counter
    }

    private fun mergeOctopusesData(
        dumboOctopuses: MutableList<MutableList<Int>>,
        copyOfDumboOctopuses: MutableList<MutableList<Int>>
    ) = dumboOctopuses.indices.forEach { x -> dumboOctopuses[x].indices.forEach { y ->
            if (dumboOctopuses[x][y] != 0) dumboOctopuses[x][y] = copyOfDumboOctopuses[x][y]
            else copyOfDumboOctopuses[x][y] = dumboOctopuses[x][y]
        }
    }
}
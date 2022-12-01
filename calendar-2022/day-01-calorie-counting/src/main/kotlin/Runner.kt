import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = Utils.readFile(Path("calendar-2022/day-01-calorie-counting/src/main/resources/data.txt"))
        val inventoryPerElf= getInventoryPerElf(data)

        println("Result for part I: ${getSumOfMaxCaloriesCarryingByTopElves(inventoryPerElf, 1)}")
        println("Result for part II: ${getSumOfMaxCaloriesCarryingByTopElves(inventoryPerElf, 3)}")
    }

    private fun getInventoryPerElf(data: List<String>): List<List<Int>> {
        val inventoryPerElf = mutableListOf<MutableList<Int>>()

        var index = 0
        inventoryPerElf.add(index, mutableListOf())

        data.forEach {
            if(it == "") {
                inventoryPerElf.add(++index, mutableListOf())
            } else {
                inventoryPerElf[index].add(it.toInt())
            }
        }

        return inventoryPerElf
    }

    private fun getSumOfMaxCaloriesCarryingByTopElves(inventory: List<List<Int>>, nTop: Int): Int =
        inventory.map { it.sum() }.sortedDescending().subList(0, nTop).sum()
}
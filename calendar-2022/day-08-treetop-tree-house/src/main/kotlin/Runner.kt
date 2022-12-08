import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val forest =
            Utils.readFile(Path("calendar-2022/day-08-treetop-tree-house/src/main/resources/data.txt"))
                .map { it.map { it.digitToInt() } }

        println("Result for part I: ${forest.getNumberOfVisibleTrees()}")
        println("Result for part II: ${forest.getMaxScenicScore()}")
    }

    private fun List<List<Int>>.getNumberOfVisibleTrees() = this.mapIndexed { rIndex, row ->
        row.filterIndexed { cIndex, _ -> this.isTreeVisible(Pair(rIndex, cIndex)) }.count()
    }.sum()

    private fun List<List<Int>>.isTreeVisible(position: Pair<Int, Int>) = this.isTreeOnEdge(position)
        || this.createSitesOfTree(position).values
        .any { it.isTreeVisibleFromSite(this[position.first][position.second]) }

    private fun List<List<Int>>.isTreeOnEdge(position: Pair<Int, Int>) =
        position.first == 0
            || position.second == 0
            || position.first == this.size - 1
            || position.second == this[0].size - 1

    private fun List<List<Int>>.createSitesOfTree(position: Pair<Int, Int>) = with(this.map { it[position.second] }) {
        mapOf(
            Site.LEFT to this@createSitesOfTree[position.first].subList(0, position.second),
            Site.RIGHT to this@createSitesOfTree[position.first].subList(
                position.second + 1,
                this@createSitesOfTree[position.first].size
            ),
            Site.UP to this@with.subList(0, position.first),
            Site.DOWN to this@with.subList(position.first + 1, this@with.size)
        )
    }

    private fun List<Int>.isTreeVisibleFromSite(treeHeight: Int) = this.max() < treeHeight

    private fun List<List<Int>>.getMaxScenicScore() = this.mapIndexed { rIndex, row ->
        List(row.size) { cIndex -> this.getScenicScoreOfTree(Pair(rIndex, cIndex)) }.max()
    }.max()

    private fun  List<List<Int>>.getScenicScoreOfTree(position: Pair<Int, Int>) =
        if (this.isTreeVisible(position)) {
            val sitesOfTree = this.createSitesOfTree(position)

            val left = sitesOfTree[Site.LEFT]!!.reversed()
                .getViewRangeFromOneSite(this[position.first][position.second])

            val right = sitesOfTree[Site.RIGHT]!!
                .getViewRangeFromOneSite(this[position.first][position.second])

            val up = sitesOfTree[Site.UP]!!.reversed()
                .getViewRangeFromOneSite(this[position.first][position.second])

            val down = sitesOfTree[Site.DOWN]!!
                .getViewRangeFromOneSite(this[position.first][position.second])

            left * right * up * down
        } else 1


    private fun List<Int>.getViewRangeFromOneSite(treeHeight: Int) =
        this.indexOfFirst { it >= treeHeight }
            .let { if (it == -1) this.size else it + 1 }

    private enum class Site {
        LEFT, RIGHT, UP, DOWN;
    }
}
import Utils.splitLineBy
import kotlin.io.path.Path

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        val pairsOfSectionAssignments =
            Utils.readFile(Path("calendar-2022/day-04-camp-cleanup/src/main/resources/data.txt"))
                .splitLineBy(",")
                .map { pairSectionsAssignment ->
                    pairSectionsAssignment[0].split("-").let { (it[0].toInt()..it[1].toInt()).toSet() } to
                        pairSectionsAssignment[1].split("-").let { (it[0].toInt()..it[1].toInt()).toSet() }

                }

        println("Result for part I: ${countFullyOverlappingSections(pairsOfSectionAssignments)}")
        println("Result for part II: ${countAtLeastPartialOverlappingSections(pairsOfSectionAssignments)}")
    }

    private fun countFullyOverlappingSections(pairsOfSectionAssignments: List<Pair<Set<Int>, Set<Int>>>) =
        pairsOfSectionAssignments.count {
            with(it.first.union(it.second).size) {
                this == it.first.size || this == it.second.size
            }
        }

    private fun countAtLeastPartialOverlappingSections(pairsOfSectionAssignments: List<Pair<Set<Int>, Set<Int>>>) =
        pairsOfSectionAssignments.count {
            it.first.intersect(it.second).isNotEmpty()
        }

}
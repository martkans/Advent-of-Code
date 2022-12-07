package model

sealed interface FilesystemObject {
    val name: String
    val parent: Directory?
    var calculatedSize: Long?
    fun calculateSize(): Long

}

class File(
    override val name: String,
    override val parent: Directory?,
    private val size: Long,
    override var calculatedSize: Long? = null
) :
    FilesystemObject {
    override fun calculateSize(): Long {
        calculatedSize = size
        return calculatedSize!!
    }
}

class Directory(
    override val name: String,
    override val parent: Directory?,
    val objects: MutableList<FilesystemObject>,
    override var calculatedSize: Long? = null
) : FilesystemObject {
    override fun calculateSize(): Long {
        calculatedSize = objects.sumOf { it.calculateSize() }
        return calculatedSize!!
    }

    fun getSumOfSizesOfAllCategoriesExactlyOrBelowLimit(sizeLimit: Long): Long = if (calculatedSize!! <= sizeLimit) {
        calculatedSize!! + objects.filterIsInstance(Directory::class.java)
            .sumOf { it.getSumOfSizesOfAllCategoriesExactlyOrBelowLimit(sizeLimit) }
    } else {
        objects.filterIsInstance(Directory::class.java)
            .sumOf { it.getSumOfSizesOfAllCategoriesExactlyOrBelowLimit(sizeLimit) }
    }

    fun getSmallestDirectoryToDelete(minSize: Long): Long? {
        val smallestChildDir = objects.filterIsInstance(Directory::class.java)
            .mapNotNull { it.getSmallestDirectoryToDelete(minSize) }.toMutableList()

        if (calculatedSize!! >= minSize)
            smallestChildDir.add(calculatedSize!!)

        return smallestChildDir.minOrNull()

    }
}

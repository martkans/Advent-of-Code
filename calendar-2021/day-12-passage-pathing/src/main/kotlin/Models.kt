
enum class CaveType {
    START, BIG, SMALL, END
}

data class Cave(val name: String, val type: CaveType, val tunnels: MutableList<Cave> = mutableListOf(), val visited: Boolean = false) {
    override fun toString(): String {
        return name
    }
}

//data class Tunnel(val name: String, val cave1: Cave, val cave2: Cave) {
//    override fun toString(): String {
//        return name
//    }
//}
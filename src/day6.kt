import java.io.File

fun day6a() {
    val groups = getGroups()
    println("Count of distinct yesses per group is ${groups.sumBy { it.getDistinctYesses() }}")
}

fun day6b() {
    val groups = getGroups()
    println("Count of unanymous yesses per group is ${groups.sumBy { it.getUnanimousYesses() }}")
}

private fun getGroups(): List<Group> {
    val file = File("./assets/day6.txt")
    val groups = mutableListOf<Group>()
    var currentGroup = Group()
    groups.add(currentGroup)
    file.forEachLine {
        if (it.isEmpty()) {
            currentGroup = Group()
            groups.add(currentGroup)
        } else {
            currentGroup.entries.add(it)
        }
    }
    return groups
}

// list of groups

class Group(
    val entries: MutableList<String> = mutableListOf()
) {
    fun getDistinctYesses(): Int {
        return entries.reduce { acc, s -> acc + s }
            .toCharArray()
            .distinct()
            .size
    }

    fun getUnanimousYesses(): Int {
         return entries.foldIndexed("") { index, acc, s ->
             if (index == 0) {
                 s
             } else {
                 acc.filter { s.contains(it) }
             }
         }.length
    }
}

// list of entries

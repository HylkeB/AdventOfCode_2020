import java.io.File

fun day10a() {
    var stepsOfOne = 0
    var stepsOfThree = 1 // final step is to the device, but i skip that but that is defined as a step of three
    var currentJoltage = 0
    val adapters = getAdapters()
    adapters.forEach {
        val stepSize = it.joltage - currentJoltage
        if (stepSize == 3) {
            stepsOfThree++
        } else if (stepSize == 1) {
            stepsOfOne++
        }
        currentJoltage = it.joltage
    }
    println("the 1 step joltage differences ($stepsOfOne) multiplied by the 3 step joltage differences ($stepsOfThree) is ${stepsOfOne * stepsOfThree}")
}

fun day10b() {
    val adapters = getAdapters()
    val indexScoreCache = hashMapOf<Int, Long>()
    println("The amount of options to arrange the adapters is ${countUniqueAdapterPathsFromPosition(adapters[0], indexScoreCache)}")
}

// TODO later add some cache so we dont have to repeatedly calculate the same end-path
private fun countUniqueAdapterPathsFromPosition(currentAdapter: Adapter, indexScoreCache: HashMap<Int, Long>) : Long {
    val cachedScore = indexScoreCache[currentAdapter.index]
    if (cachedScore != null) {
        return cachedScore
    }

    if (currentAdapter.possibleNextAdapters.isEmpty()) {
        return 1L
    }

    var childOptions = 0L
    currentAdapter.possibleNextAdapters.forEach {
        childOptions += countUniqueAdapterPathsFromPosition(it, indexScoreCache)
    }
    indexScoreCache[currentAdapter.index] = childOptions
    return childOptions
}


private fun getAdapters(): List<Adapter> {
    val file = File("/Users/hylkebron/Projects/AdventOfCode_2020/assets/day10.txt")
    val adapters = mutableListOf<Adapter>()
    adapters.add(Adapter(0))
    file.forEachLine {
        val currentAdapter = Adapter(it.toInt())
        adapters.add(currentAdapter)
    }
    return adapters
        .sortedBy { it.joltage }
        .apply {
            forEachIndexed { index, adapter ->
                adapter.index = index
                val previous1 = index - 1
                val previous2 = index - 2
                val previous3 = index - 3
                if (previous1 >= 0 && adapter.joltage - this[previous1].joltage <= 3) {
                    this[previous1].possibleNextAdapters.add(adapter)
                }
                if (previous2 >= 0 && adapter.joltage - this[previous2].joltage <= 3) {
                    this[previous2].possibleNextAdapters.add(adapter)
                }
                if (previous3 >= 0 && adapter.joltage - this[previous3].joltage <= 3) {
                    this[previous3].possibleNextAdapters.add(adapter)
                }
            }
        }
}

class Adapter(
    val joltage: Int
) {
    val possibleNextAdapters = ArrayList<Adapter>()
    var index: Int = 0
}
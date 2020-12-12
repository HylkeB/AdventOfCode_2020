import java.io.File

fun day3a() {

    val rowsOfEnvironment = getRowsOfEnvironment()
        .apply { removeAt(0) }
    var currentHorizontalIndex = 0
    var amountOfTrees = 0
    rowsOfEnvironment.forEach { row ->
        currentHorizontalIndex += 3
        currentHorizontalIndex %= row.size
        if (row[currentHorizontalIndex] == EnvironmentSquare.Tree) {
            amountOfTrees++
        }
    }

    println("There are $amountOfTrees trees along the way")
}

fun day3b() {
    val environment = getRowsOfEnvironment()
    val right1down1 = getTreesForSlope(1, 1, environment)
    val right3down1 = getTreesForSlope(3, 1, environment)
    val right5down1 = getTreesForSlope(5, 1, environment)
    val right7down1 = getTreesForSlope(7, 1, environment)
    val right1down2 = getTreesForSlope(1, 2, environment)
    println("Right 1 down 1 = $right1down1")
    println("Right 3 down 1 = $right3down1")
    println("Right 5 down 1 = $right5down1")
    println("Right 7 down 1 = $right7down1")
    println("Right 1 down 2 = $right1down2")
    println("Multiplied = ${right1down1 * right3down1 * right5down1 * right7down1 * right1down2}")
}

private fun getTreesForSlope(right: Int, down: Int, environment: MutableList<List<EnvironmentSquare>>): Int {
    var amountOfTrees = 0
    var currentHorizontalIndex = 0
    for (y in down until environment.size step down) {
        val row = environment[y]
        currentHorizontalIndex += right
        currentHorizontalIndex %= row.size
        if (row[currentHorizontalIndex] == EnvironmentSquare.Tree) {
            amountOfTrees ++
        }
    }
    return amountOfTrees
}

private fun getRowsOfEnvironment(): MutableList<List<EnvironmentSquare>> {
    val file = File("./assets/day3.txt")
    val rowsOfEnvironment = mutableListOf<List<EnvironmentSquare>>()
    file.forEachLine {
        val row = it.map { char ->
            if (char == '.') {
                EnvironmentSquare.Open
            } else {
                EnvironmentSquare.Tree
            }
        }
        rowsOfEnvironment.add(row)
    }
    return rowsOfEnvironment
}

sealed class EnvironmentSquare {
    object Open : EnvironmentSquare()
    object Tree : EnvironmentSquare()
}
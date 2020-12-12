import java.io.File

fun day11a() {
    val spots = getSpots()
    var iterationCount = 0
    printSpots(spots, iterationCount)
    var spotsToMutate = getIndexesOfSpotsToMutateForAdjacentNeighbors(spots)
    while (spotsToMutate.isNotEmpty()) {
        iterationCount++
        spotsToMutate.forEach { index ->
            spots[index].type = if (spots[index].type == SpotType.OCCUPIED_SEAT) {
                SpotType.EMPTY_SEAT
            } else {
                SpotType.OCCUPIED_SEAT
            }
        }
        printSpots(spots, iterationCount)
        spotsToMutate = getIndexesOfSpotsToMutateForAdjacentNeighbors(spots)
    }
    println("After $iterationCount iterations the chaos stabilizes, there are ${spots.count { it.type == SpotType.OCCUPIED_SEAT }} occupied spots")
}

fun day11b() {
    val spots = getSpots()
    var iterationCount = 0
    printSpots(spots, iterationCount)
    var spotsToMutate = getMutationsForVisibleNeighbors(spots)
    while (spotsToMutate.isNotEmpty()) {
        iterationCount++
        spotsToMutate.forEach { index ->
            spots[index].type = if (spots[index].type == SpotType.OCCUPIED_SEAT) {
                SpotType.EMPTY_SEAT
            } else {
                SpotType.OCCUPIED_SEAT
            }
        }
        printSpots(spots, iterationCount)
        spotsToMutate = getMutationsForVisibleNeighbors(spots)
    }
    println("After $iterationCount iterations the chaos stabilizes, there are ${spots.count { it.type == SpotType.OCCUPIED_SEAT }} occupied spots")
}

private fun printSpots(spots: List<Spot>, iteration: Int) {
    println("${System.lineSeparator()}After $iteration iterations this is how the seats look:")
    val rowLength = Math.sqrt(spots.size.toDouble()).toInt()
    spots.forEachIndexed { index, spot ->
        if (index % rowLength == 0) {
            print(System.lineSeparator())
        }
        when (spot.type) {
            SpotType.FLOOR -> print(".")
            SpotType.EMPTY_SEAT -> print("L")
            SpotType.OCCUPIED_SEAT -> print("#")
        }
    }
    println()
    println()
}

private fun getMutationsForVisibleNeighbors(spots: List<Spot>): List<Int> {
    val spotsToMutate = mutableListOf<Int>()
    spots.forEachIndexed { index, spot ->
        if (spot.type == SpotType.FLOOR) {
            return@forEachIndexed
        }
        val occupiedNeighbors = spot.visibleSpots.count { it.type == SpotType.OCCUPIED_SEAT }
        if (spot.type == SpotType.EMPTY_SEAT) {
            if (occupiedNeighbors == 0) {
                spotsToMutate.add(index)
            }
        }
        if (spot.type == SpotType.OCCUPIED_SEAT) {
            if (occupiedNeighbors >= 5) {
                spotsToMutate.add(index)
            }
        }
    }
    return spotsToMutate
}


private fun getIndexesOfSpotsToMutateForAdjacentNeighbors(spots: List<Spot>): List<Int> {
    val spotsToMutate = mutableListOf<Int>()
    spots.forEachIndexed { index, spot ->
        if (spot.type == SpotType.FLOOR) {
            return@forEachIndexed
        }
        val occupiedNeighbors = spot.adjacentSpots.count { it.type == SpotType.OCCUPIED_SEAT }
        if (spot.type == SpotType.EMPTY_SEAT) {
            if (occupiedNeighbors == 0) {
                spotsToMutate.add(index)
            }
        }
        if (spot.type == SpotType.OCCUPIED_SEAT) {
            if (occupiedNeighbors >= 4) {
                spotsToMutate.add(index)
            }
        }
    }
    return spotsToMutate
}

private fun getSpots(): List<Spot> {
    val file = File("./assets/day11.txt")
    val spots = mutableListOf<Spot>()
    var rowIndex = 0
    file.forEachLine {  row ->
        row.forEachIndexed { columnIndex, char ->
            val type = when (char) {
                '.' -> SpotType.FLOOR
                else -> SpotType.EMPTY_SEAT
            }
            val currentSpot = Spot(type)
            spots.add(currentSpot)

            if (columnIndex > 0) {
                val leftSpot = spots[getIndex(rowIndex, columnIndex - 1, row.length)]
                currentSpot.adjacentSpots.add(leftSpot)
                leftSpot.adjacentSpots.add(currentSpot)
                val leftVisibleChair = lookLeft(spots, rowIndex, columnIndex - 1, row.length)
                if (leftVisibleChair != null) {
                    currentSpot.visibleSpots.add(leftVisibleChair)
                    leftVisibleChair.visibleSpots.add(currentSpot)
                }
            }

            if (rowIndex > 0) {
                val topSpot = spots[getIndex(rowIndex - 1, columnIndex, row.length)]
                currentSpot.adjacentSpots.add(topSpot)
                topSpot.adjacentSpots.add(currentSpot)
                val topVisibleChair = lookUp(spots, rowIndex - 1, columnIndex, row.length)
                if (topVisibleChair != null) {
                    currentSpot.visibleSpots.add(topVisibleChair)
                    topVisibleChair.visibleSpots.add(currentSpot)
                }
            }

            if (rowIndex > 0 && columnIndex > 0) {
                val topLeftSpot = spots[getIndex(rowIndex - 1, columnIndex - 1, row.length)]
                currentSpot.adjacentSpots.add(topLeftSpot)
                topLeftSpot.adjacentSpots.add(currentSpot)
                val leftUpVisibleChair = lookLeftUp(spots, rowIndex - 1, columnIndex - 1, row.length)
                if (leftUpVisibleChair != null) {
                    currentSpot.visibleSpots.add(leftUpVisibleChair)
                    leftUpVisibleChair.visibleSpots.add(currentSpot)
                }
            }

            if (rowIndex > 0 && columnIndex < row.length - 1) {
                val topRightSpot = spots[getIndex(rowIndex - 1, columnIndex + 1, row.length)]
                currentSpot.adjacentSpots.add(topRightSpot)
                topRightSpot.adjacentSpots.add(currentSpot)
                val rightUpVisibleChair = lookRightUp(spots, rowIndex - 1, columnIndex + 1, row.length)
                if (rightUpVisibleChair != null) {
                    currentSpot.visibleSpots.add(rightUpVisibleChair)
                    rightUpVisibleChair.visibleSpots.add(currentSpot)
                }
            }
        }
        rowIndex++
    }
    return spots
}

private tailrec fun lookLeft(spots: List<Spot>, rowIndex: Int, columnIndex: Int, rowLength: Int): Spot? {
    val currentIndex = getIndex(rowIndex, columnIndex, rowLength)
    if (spots[currentIndex].type != SpotType.FLOOR) {
        return spots[currentIndex]
    }
    val nextColumnIndex = if (columnIndex > 0) {
        columnIndex - 1
    } else {
        return null
    }

    return lookLeft(spots, rowIndex, nextColumnIndex, rowLength)
}

private tailrec fun lookUp(spots: List<Spot>, rowIndex: Int, columnIndex: Int, rowLength: Int): Spot? {
    val currentIndex = getIndex(rowIndex, columnIndex, rowLength)
    if (spots[currentIndex].type != SpotType.FLOOR) {
        return spots[currentIndex]
    }
    val nextRowIndex = if (rowIndex > 0) {
        rowIndex - 1
    } else {
        return null
    }

    return lookUp(spots, nextRowIndex, columnIndex, rowLength)
}

private tailrec fun lookLeftUp(spots: List<Spot>, rowIndex: Int, columnIndex: Int, rowLength: Int): Spot? {
    val currentIndex = getIndex(rowIndex, columnIndex, rowLength)
    if (spots[currentIndex].type != SpotType.FLOOR) {
        return spots[currentIndex]
    }
    val nextRowIndex = if (rowIndex > 0) {
        rowIndex - 1
    } else {
        return null
    }
    val nextColumnIndex = if (columnIndex > 0) {
        columnIndex - 1
    } else {
        return null
    }

    return lookLeftUp(spots, nextRowIndex, nextColumnIndex, rowLength)
}

private tailrec fun lookRightUp(spots: List<Spot>, rowIndex: Int, columnIndex: Int, rowLength: Int): Spot? {
    val currentIndex = getIndex(rowIndex, columnIndex, rowLength)
    if (spots[currentIndex].type != SpotType.FLOOR) {
        return spots[currentIndex]
    }
    val nextRowIndex = if (rowIndex > 0) {
        rowIndex - 1
    } else {
        return null
    }
    val nextColumnIndex = if (columnIndex < rowLength - 1) {
        columnIndex + 1
    } else {
        return null
    }

    return lookRightUp(spots, nextRowIndex, nextColumnIndex, rowLength)
}

private fun getIndex(rowIndex: Int, columnIndex: Int, rowLength: Int): Int {
    return rowIndex * rowLength + columnIndex
}

class Spot(
    var type: SpotType
) {
    var adjacentSpots = mutableListOf<Spot>()
    var visibleSpots = mutableListOf<Spot>()
}

enum class SpotType { FLOOR, EMPTY_SEAT, OCCUPIED_SEAT }

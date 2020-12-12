import java.io.File

fun day9a() {
    val numbers = getNumbers()
    numbers.forEachIndexed { index, number ->
        if (index >= 25) {
            if (!isNumberSumOfTwoInRange(number, numbers.subList(index - 25, index))) {
                println("The first number that is not the sum of 2 of the previous 25 numbers is $number (index is $index)")
            }
        }
    }
}

fun day9b() {
    val targetContiguousSum = 756008079L
    val numbers = getNumbers()

    numbers.forEachIndexed outer@{ outerIndex, outerNumber ->
        if (outerNumber < targetContiguousSum) {
            val remainingNumbers = numbers.subList(outerIndex + 1, numbers.size)
            for (innerIndex in 0 until remainingNumbers.size) {
                val listToCheck = remainingNumbers.subList(0, innerIndex + 1)
                val sum = listToCheck.sum()
                if (sum == targetContiguousSum) {
                    println("We found the sum using list: $listToCheck")
                    val sorted = listToCheck.sorted()
                    println("Smallest and highest together are ${sorted.first() + sorted.last()}")
                }
            }
        }
    }

}

private fun isNumberSumOfTwoInRange(number: Long, range: List<Long>): Boolean {
    range.forEachIndexed { indexOne, one ->
        range.forEachIndexed { indexTwo, two ->
            if (indexOne != indexTwo) {
                if (one + two == number) {
                    return true
                }
            }
        }
    }
    return false
}

private fun getNumbers(): List<Long> {
    val file = File("/Users/hylkebron/Projects/AdventOfCode_2020/assets/day9.txt")
    val numbers = mutableListOf<Long>()
    file.forEachLine {
        numbers.add(it.toLong())
    }
    return numbers.toList()
}
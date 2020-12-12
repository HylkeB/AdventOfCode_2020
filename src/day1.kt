import java.io.File

fun day1a() {
    val file = File("/Users/hylkebron/Projects/AdventOfCode_2020/assets/day1.txt")
    val numbers = mutableListOf<Int>()
    file.forEachLine {
        numbers.add(it.toInt())
    }

    numbers.forEach { first ->
        numbers.forEach { second ->
            if (first + second == 2020) {
                println("$first * $second = ${first * second}")
            }
        }
    }
}

fun day1b() {
    val file = File("/Users/hylkebron/Projects/AdventOfCode_2020/assets/day1.txt")
    val numbers = mutableListOf<Int>()
    file.forEachLine {
        numbers.add(it.toInt())
    }

    numbers.forEach { first ->
        numbers.forEach { second ->
            numbers.forEach { third ->
                if (first + second + third == 2020) {
                    println("$first * $second * $third = ${first * second * third}")
                }
            }
        }
    }
}
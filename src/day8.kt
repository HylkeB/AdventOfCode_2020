import java.io.File

fun day8a() {
    val instructions = getListOfInstructions()
    fun getNextInstruction(currentInstruction: Instruction): Instruction {
        return instructions[currentInstruction.getNextIndex()]
    }
    var instruction = instructions[0]
    var accumulator = 0
    while (!instruction.visited) {
        if (instruction is Instruction.Accumulate) {
            accumulator += instruction.value
        }
        instruction.visited = true
        instruction = getNextInstruction(instruction)
    }
    println("Value of accumulator is $accumulator")
}

fun day8b() {
    val rawInstructions = getListOfInstructions()

    fun performRun(instructions: List<Instruction>): Boolean {
        instructions.forEach { it.visited = false }
        fun getNextInstruction(currentInstruction: Instruction): Instruction? {
            return instructions.getOrNull(currentInstruction.getNextIndex())
        }

        var instruction: Instruction? = instructions[0]
        var accumulator = 0
        while (instruction != null && !instruction.visited) {
            if (instruction is Instruction.Accumulate) {
                accumulator += instruction.value
            }
            instruction.visited = true
            instruction = getNextInstruction(instruction)
        }
        val terminated = instruction == null
        println("Value of accumulator is $accumulator. Did we have terminate? $terminated")
        return terminated
    }

    rawInstructions.forEachIndexed { index, it ->
        val terminated = when (it) {
            is Instruction.Noop -> {
                val copy = rawInstructions.toMutableList()
                copy[index] = Instruction.Jump(it.currentIndex, it.value)
                performRun(copy)
            }
            is Instruction.Jump -> {
                val copy = rawInstructions.toMutableList()
                copy[index] = Instruction.Noop(it.currentIndex, it.value)
                performRun(copy)
            }
            else -> false
        }
        if (terminated) {
            return
        }
    }
}

private fun getListOfInstructions(): List<Instruction> {
    val file = File("/Users/hylkebron/Projects/AdventOfCode_2020/assets/day8.txt")
    val instructions = mutableListOf<Instruction>()
    var index = 0
    file.forEachLine {
        val opCode = it.split(" ")[0]
        val value = it.split(" ")[1].toInt()
        val instruction = when (opCode) {
            "jmp" -> Instruction.Jump(index, value)
            "nop" -> Instruction.Noop(index, value)
            "acc" -> Instruction.Accumulate(index, value)
            else -> null
        }
        index++
        if (instruction != null) {
            instructions.add(instruction)
        }
    }
    return instructions
}

sealed class Instruction(
    val currentIndex: Int
) {
    var visited = false

    open fun getNextIndex(): Int {
        return currentIndex + 1
    }

    class Noop(index: Int, val value: Int) : Instruction(index)
    class Accumulate(index: Int, val value: Int) : Instruction(index)
    class Jump(index: Int, val value: Int) : Instruction(index) {
        override fun getNextIndex(): Int {
            return currentIndex + value
        }
    }
}
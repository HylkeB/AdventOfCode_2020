import java.io.File
import kotlin.math.abs

fun day12a() {
    val instructions = getShipInstructions()

    val shipInfo = ShipInfo(0, 0, 0, 10, 1)

    val finalShipInfo = instructions.fold(shipInfo) { accumulatedInfo, currentShipInstruction ->
        currentShipInstruction.executeQuestionA(accumulatedInfo)
    }

    println("The ship eventually moved to $finalShipInfo. The manhattan distance is ${abs(finalShipInfo.x) + abs(finalShipInfo.y)}")
}

fun day12b() {
    val instructions = getShipInstructions()

    val shipInfo = ShipInfo(0, 0, 0, 10, 1)

    val finalShipInfo = instructions.fold(shipInfo) { accumulatedInfo, currentShipInstruction ->
        currentShipInstruction.executeQuestionB(accumulatedInfo)
    }

    println("The ship eventually moved to $finalShipInfo. The manhattan distance is ${abs(finalShipInfo.x) + abs(finalShipInfo.y)}")
}

private fun getShipInstructions(): List<ShipInstruction> {
    val file = File("/Users/hylkebron/Projects/AdventOfCode_2020/assets/day12.txt")
    val instructions = mutableListOf<ShipInstruction>()
    file.forEachLine {
        val instructionChar = it[0]
        val value = it.substring(1).toInt()
        val instruction = when (instructionChar) {
            'N' -> ShipInstruction.North(value)
            'E' -> ShipInstruction.East(value)
            'S' -> ShipInstruction.South(value)
            'W' -> ShipInstruction.West(value)
            'L' -> ShipInstruction.RotateLeft(value)
            'R' -> ShipInstruction.RotateRight(value)
            'F' -> ShipInstruction.Forward(value)
            else -> throw IllegalStateException("Unknown ship instruction: $it")
        }
        instructions.add(instruction)
    }
    return instructions
}

data class ShipInfo(
    val x: Int,
    val y: Int,
    val orientation: Int, // only for question a
    val waypointX: Int,
    val waypointY: Int
)

sealed class ShipInstruction {
    abstract fun executeQuestionA(shipInfo: ShipInfo): ShipInfo
    abstract fun executeQuestionB(shipInfo: ShipInfo): ShipInfo

    class North(private val amount: Int) : ShipInstruction() {
        override fun executeQuestionA(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(y = shipInfo.y + amount)
        }

        override fun executeQuestionB(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(waypointY = shipInfo.waypointY + amount)
        }
    }

    class East(private val amount: Int) : ShipInstruction() {
        override fun executeQuestionA(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(x = shipInfo.x + amount)
        }

        override fun executeQuestionB(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(waypointX = shipInfo.waypointX + amount)
        }
    }

    class South(private val amount: Int) : ShipInstruction() {
        override fun executeQuestionA(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(y = shipInfo.y - amount)
        }

        override fun executeQuestionB(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(waypointY = shipInfo.waypointY - amount)
        }
    }

    class West(private val amount: Int) : ShipInstruction() {
        override fun executeQuestionA(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(x = shipInfo.x - amount)
        }

        override fun executeQuestionB(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(waypointX = shipInfo.waypointX - amount)
        }
    }

    class RotateLeft(private val amount: Int) : ShipInstruction() {
        override fun executeQuestionA(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(orientation = shipInfo.orientation + amount)
        }

        override fun executeQuestionB(shipInfo: ShipInfo): ShipInfo {
            // east -> north -> west -> south -> east -> ...
            var currentShipInfo = shipInfo
            repeat(amount / 90) {
                currentShipInfo = moveLeftOnce(currentShipInfo)
            }
            return currentShipInfo
        }

        private fun moveLeftOnce(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(
                waypointX = shipInfo.waypointY * -1,
                waypointY = shipInfo.waypointX
            )
        }
    }

    class RotateRight(private val amount: Int) : ShipInstruction() {
        override fun executeQuestionA(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(orientation = shipInfo.orientation - amount)
        }

        override fun executeQuestionB(shipInfo: ShipInfo): ShipInfo {
            // east -> south -> west -> north -> east -> ...
            var currentShipInfo = shipInfo
            repeat(amount / 90) {
                currentShipInfo = moveRightOnce(currentShipInfo)
            }
            return currentShipInfo
        }

        private fun moveRightOnce(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(
                waypointX = shipInfo.waypointY,
                waypointY = shipInfo.waypointX * -1
            )
        }
    }

    class Forward(private val amount: Int) : ShipInstruction() {
        override fun executeQuestionA(shipInfo: ShipInfo): ShipInfo {
            var normalizedOrientation = shipInfo.orientation % 360
            if (normalizedOrientation < 0) {
                normalizedOrientation += 360
            }
            return when (normalizedOrientation) {
                0 -> shipInfo.copy(x = shipInfo.x + amount)
                90 -> shipInfo.copy(y = shipInfo.y + amount)
                180 -> shipInfo.copy(x = shipInfo.x - amount)
                270 -> shipInfo.copy(y = shipInfo.y - amount)
                else -> throw IllegalStateException("Unrecognized normalized orientation value: $normalizedOrientation. Original is ${shipInfo.orientation}")
            }
        }

        override fun executeQuestionB(shipInfo: ShipInfo): ShipInfo {
            return shipInfo.copy(
                x = shipInfo.x + shipInfo.waypointX * amount,
                y = shipInfo.y + shipInfo.waypointY * amount
            )
        }
    }
}
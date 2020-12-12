import java.io.File

fun day5a() {
    val tickets = getTickets()
    val highestTicketId = tickets.map { it.getId() }
        .max()
    println("highest ticket id is $highestTicketId")

    day5b()
}

fun day5b() {
    val tickets = getTickets()
    val sorted = tickets.map { it.getId() }
        .sorted()
    sorted.forEachIndexed { index, id ->
            val nextNumber = sorted.getOrNull(index + 1) ?: 0
            if (nextNumber - id == 2) {
                println("It could be ${id+1}")
            }
        }
}

private fun getTickets(): List<Ticket> {

    val file = File("./assets/day5.txt")
    val tickets = mutableListOf<Ticket>()
    file.forEachLine {
        tickets.add(Ticket(it))
    }
    return tickets
}

class Ticket(
    val rawData: String
) {
    fun getId(): Int {
        var maxRow = 127
        var minRow = 0
        var rowStepSize = 64
        for (rowIndex in 0..6) {
            val rowIndicator = rawData[rowIndex]
            if (rowIndicator == 'F') {
                maxRow -= rowStepSize
            } else { // 'B'
                minRow += rowStepSize
            }
            rowStepSize /= 2
        }
        val row = minRow // assume maxRow is the same as minRow
        var maxColumn = 7
        var minColumn = 0
        var columnStepSize = 4
        for (columnIndex in 7..9) {
            val columnIndicator = rawData[columnIndex]
            if (columnIndicator == 'L') {
                maxColumn -= columnStepSize
            } else { // 'R'
                minColumn += columnStepSize
            }
            columnStepSize /= 2
        }
        val column = minColumn // assume maxColumn is the same as minColumn
        return row * 8 + column
    }
}
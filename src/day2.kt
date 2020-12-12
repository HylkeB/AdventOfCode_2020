import java.io.File

fun day2a() {
    val validEntries = getEntries().count { it.isValidForSledRental() }
    println("Valid passwords: $validEntries")
}

fun day2b() {
    val validEntries = getEntries().count { it.isValidForToboggan() }
    println("Valid passwords: $validEntries")
}

fun getEntries(): List<PasswordEntry> {
    val file = File("./assets/day2.txt")
    val entries = mutableListOf<PasswordEntry>()
    file.forEachLine {
        val firstNumber = it.split("-")[0].toInt()
        val secondNumber = it.split("-")[1].split(" ")[0].toInt()
        val char = it.split(" ")[1].split(":")[0][0]
        val password = it.split(" ")[2]
        entries.add(
            PasswordEntry(
                PasswordPolicy(
                    firstNumber,
                    secondNumber,
                    char
                ),
                password
            )
        )
    }
    return entries
}

class PasswordEntry(
    val passwordPolicy: PasswordPolicy,
    val password: String
) {
    fun isValidForSledRental(): Boolean {
        val amountOfPolicyChars = password.count { it == passwordPolicy.char }
        return amountOfPolicyChars in passwordPolicy.firstNumber..passwordPolicy.secondNumber
    }

    fun isValidForToboggan(): Boolean {
        val charAtFirstNumberIsPolicyChar = password[passwordPolicy.firstNumber - 1] == passwordPolicy.char
        val charAtSecondNumberIsPolicyChar = password[passwordPolicy.secondNumber - 1] == passwordPolicy.char
        if (charAtFirstNumberIsPolicyChar && charAtSecondNumberIsPolicyChar) {
            return false
        }
        if (!charAtFirstNumberIsPolicyChar && !charAtSecondNumberIsPolicyChar) {
            return false
        }
        return true
    }
}

class PasswordPolicy(
    val firstNumber: Int,
    val secondNumber: Int,
    val char: Char
)
import java.io.File

fun day4a() {
    val passports = readPasswords()
    println("Valid passports: ${passports.count { it.isValid() }}")
}

private fun readPasswords(): List<Passport> {
    val file = File("/Users/hylkebron/Projects/AdventOfCode_2020/assets/day4.txt")

    val passports = mutableListOf(Passport())
    file.forEachLine {
        if (it.isEmpty()) {
            passports.add(Passport())
        } else {
            val currentPassport = passports.last()
            val pairs = it.split(" ")
            pairs.forEach { pair ->
                val key = pair.split(":")[0]
                val value = pair.split(":")[1]
                when (key) {
                    "byr" -> currentPassport.birthYear = value.toInt()
                    "iyr" -> currentPassport.issueYear = value.toInt()
                    "eyr" -> currentPassport.expirationYear = value.toInt()
                    "hgt" -> currentPassport.height = value
                    "hcl" -> currentPassport.hairColor = value
                    "ecl" -> currentPassport.eyeColor = value
                    "pid" -> currentPassport.passportId = value
                    "cid" -> currentPassport.countryId = value
                }
            }
        }
    }
    return passports
}

data class Passport(
    var birthYear: Int? = null,
    var issueYear: Int? = null,
    var expirationYear: Int? = null,
    var height: String? = null,
    var hairColor: String? = null,
    var eyeColor: String? = null,
    var passportId: String? = null,
    var countryId: String? = null
) {

    fun isValid(): Boolean {
        return birthYear ?: 0 in 1920..2002
                && issueYear ?: 0 in 2010..2020
                && expirationYear ?: 0 in 2020..2030
                && isHeightValid(height)
                && isValidHairColor(hairColor)
                && isValidEyeColor(eyeColor)
                && isValidPassportId(passportId)
    }

    private fun isHeightValid(height: String?): Boolean {
        if (height == null) {
            return false
        }

        return if (height.contains("cm")) {
            val amount = height.split("cm")[0].toInt()
            amount in 150..193
        } else {
            val amount = height.split("in")[0].toInt()
            amount in 59..76
        }
    }

    private fun isValidHairColor(hairColor: String?): Boolean {
        if (hairColor == null) {
            return false
        }

        hairColor.forEachIndexed { index, char ->
            if (index == 0 && char != '#') return false
            if (index != 0 && char !in '0'..'9' && char !in 'a'..'f') return false
        }
        return true
    }

    private fun isValidEyeColor(eyeColor: String?): Boolean {
        return when (eyeColor) {
            "amb",
            "blu",
            "brn",
            "gry",
            "grn",
            "hzl",
            "oth" -> true
            else -> false
        }
    }

    private fun isValidPassportId(passportId: String?): Boolean {
        if (passportId == null) {
            return false
        }

        return passportId.length == 9 && passportId.all {
            it in '0'..'9'
        }
    }
}
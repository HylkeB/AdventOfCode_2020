import java.io.File

fun day7a() {
    val rules = getBagRules()
    var amountOfOptions = 0
    rules.forEach { (color, bagRule) ->
        if (color == "shiny gold") {
            return@forEach
        }
        if (containsShinyGold(rules, bagRule)) {
            amountOfOptions++
        }
    }

    println("There are $amountOfOptions bags that can contain a shiny gold bag")
}

fun day7b() {
    val rules = getBagRules()
    val bagsInGoldBag = countContentsOfBagRule(rules, rules["shiny gold"] ?: error("No shiny gold bag found"))
    println("The shiny gold bag contains $bagsInGoldBag other bags")
}

private fun countContentsOfBagRule(allRules: Map<String, BagRule>, bagRule: BagRule): Int {
    val directChildBags = bagRule.contains.entries.sumBy { (_, amount) -> amount }
    var contentsOfChildBags = 0
    bagRule.contains.forEach { (color, amount) ->
        val childBagRule = allRules[color] ?: error("No bag rule found for $color")
        val childBagCount = countContentsOfBagRule(allRules, childBagRule) * amount
        contentsOfChildBags += childBagCount
    }
    return contentsOfChildBags + directChildBags
}

private fun containsShinyGold(allRules: Map<String, BagRule>, bagRule: BagRule?): Boolean {
    if (bagRule == null) {
        return false
    }
    if (bagRule.color == "shiny gold") {
        return true
    }

    return bagRule
        .contains
        .any { (color, _) ->
            containsShinyGold(allRules, allRules[color])
        }
}

private fun getBagRules(): Map<String, BagRule> {
    val file = File("/Users/hylkebron/Projects/AdventOfCode_2020/assets/day7.txt")

    val rules = hashMapOf<String, BagRule>()
    file.forEachLine { line ->
        val bagRulePart = line.split("contain")[0]
        val color = bagRulePart.split("bags")[0].trim()
        val containsPart = line.split("contain")[1]
        val contains = hashMapOf<String, Int>()
        if (!containsPart.contains("no other bags")) {
            val rawContains = containsPart.split(",")
            rawContains.forEach {
                val trimmed = it.replace("bags", "")
                    .replace("bag", "")
                    .replace(".", "")
                    .trim()
                val containsColor = trimmed.substring(1).trim()
                val amount = trimmed.substring(0, 1).toInt()
                contains[containsColor] = amount
            }
        }
        rules[color] = BagRule(color, contains)
    }
    return rules
}

class BagRule(
    val color: String,
    val contains: Map<String, Int>
)
fun main() {
    data class Card(val winningNumbers: List<Int>, val ownNumbers: List<Int>, var copies: Int = 1)

    fun parse(lines: List<String>): List<Card> {
        return lines.map { line ->
            val parts = line.substringAfter(":").trim().split("|")

            val winningNumbers = parts[0].split(" ").mapNotNull { it.trim().toIntOrNull() }
            val ownNumbers = parts[1].split(" ").mapNotNull { it.trim().toIntOrNull() }

            Card(winningNumbers, ownNumbers)
        }
    }

    fun part1(lines: List<String>): Int {
        val cards = parse(lines)
        val totalPoints = cards.sumOf {
            val cardValue = it.ownNumbers.fold(0) { acc, own ->
                if (it.winningNumbers.contains(own)) {
                    if (acc == 0) {
                        1
                    } else {
                        acc * 2
                    }
                } else {
                    acc
                }
            }
            cardValue
        }
        return totalPoints
    }

    fun part2(lines: List<String>): Int {
        val cards = parse(lines)
        cards.onEachIndexed { index, card ->
            val matchingNumbers = card.ownNumbers.count { own -> card.winningNumbers.contains(own) }
            ((index + 1)..(index + matchingNumbers)).forEach {
                cards[it].copies += card.copies
            }
        }
        return cards.sumOf { it.copies }
    }

    val testInput = readInput("Day04_sample")
    val sampleResult = part1(testInput)

    println("Test part 1: $sampleResult")
    check(sampleResult == 13)

    val input = readInput("Day04")
    println("Result part 1: ${part1(input)}")

    val testResult2 = part2(testInput)
    println("Test part 2: $testResult2")
    check(testResult2 == 30)
    println("Result part 2: ${part2(input)}")
}

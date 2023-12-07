fun main() {
    val allCards = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed()

    data class Hand(val cards: List<Char>, val bid: Int) : Comparable<Hand> {
        fun getValue(): Int {
            val jackCount = cards.count { it == 'J' }

            return when {
                isFiveOfAKind()
                        || (isFourOfAKind() && jackCount == 1)
                        || (isThreeOfAKind() && jackCount == 2)
                        || (isOnePair() && jackCount == 3)
                        || jackCount == 4 -> 7

                isFourOfAKind()
                        || (isThreeOfAKind() && jackCount == 1)
                        || (isTwoPair() && jackCount == 2)
                        || jackCount == 3 -> 6

                isFullHouse()
                        || (isTwoPair() && jackCount == 1) -> 5

                isThreeOfAKind()
                        || (isOnePair() && jackCount == 1)
                        || jackCount == 2 -> 4

                isTwoPair() -> 3
                isOnePair() || jackCount == 1 -> 2
                isHighCard() -> 1
                else -> 0
            }
        }

        override fun compareTo(other: Hand): Int {
            val selfHandValue = getValue()
            val otherHandValue = other.getValue()

            return if (selfHandValue == otherHandValue) {
                var index = 0
                var result = 0
                while (index < cards.size && index < other.cards.size) {
                    result = allCards.indexOf(cards[index]).compareTo(allCards.indexOf(other.cards[index]))
                    if (result != 0) {
                        break
                    }
                    index++
                }
                result
            } else {
                selfHandValue.compareTo(otherHandValue)
            }
        }


        fun isFiveOfAKind(): Boolean = cards.distinct().size == 1


        fun isFourOfAKind(): Boolean = cards.groupingBy { it }.eachCount().values.any { it == 4 }


        fun isFullHouse(): Boolean {
            val groups = cards.groupingBy { it }.eachCount().values.sorted()
            return groups == listOf(2, 3)
        }

        fun isThreeOfAKind(): Boolean = cards.groupingBy { it }.eachCount().values.any { it == 3 }


        fun isTwoPair(): Boolean {
            val groups = cards.groupingBy { it }.eachCount().values.sorted()
            return groups == listOf(1, 2, 2)
        }

        fun isOnePair(): Boolean = cards.groupingBy { it }.eachCount().values.any { it == 2 }

        fun isHighCard(): Boolean = cards.distinct().size == 5
    }

    fun parseHands(data: List<String>): List<Hand> {
        return data.map { line ->
            val parts = line.split(" ")
            val cards = parts[0]
            val bid = parts[1].toInt()
            Hand(cards.toCharArray().toList(), bid)
        }
    }

    fun part1(lines: List<String>): Long {
        val hands = parseHands(lines)
        val sorted = hands.sorted()

        val totalWinnings = sorted.foldIndexed(0L) { index, total, hand ->
            total + ((index + 1) * hand.bid)
        }

        return totalWinnings
    }

    val testInput = readInput("Day07_sample")
    val input = readInput("Day07")

//    val sampleResult = part1(testInput)
//    println("Test part 1: $sampleResult")
//    check(sampleResult == 6440L)
//    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
    val testResult2 = part1(testInput)
    println("Test part 2: $testResult2")
    check(testResult2 == 5905L)
    println("Result part 2: ${part1(input)}")
}

fun main() {
    fun part1(lines: List<String>): Int {
        val width = lines.first().length - 1
        val height = lines.size - 1

        var sum = 0
        for (x in 0..width) {
            var currentNumber = ""
            for (y in 0..height) {
                val cell = lines
                    .get(x)
                    .get(y)
                if (cell.isDigit()) {
                    currentNumber += cell
                    if (lines.isLastDigitOfNumber(x, y) && lines.isPartNumber(x, y, currentNumber)) {
                        // check if number is part number
                        sum += currentNumber.toInt()
                    }
                } else { // dot or symbol
                    currentNumber = ""
                }
            }
        }
        return sum
    }

    fun part2(lines: List<String>): Int {
        val width = lines.first().length - 1
        val height = lines.size - 1

        var sum = 0
        for (x in 0..width) {
            for (y in 0..height) {
                val cell = lines.get(x, y, '.')
                if (cell == '*') {
                    val numbers = lines.getAdjacentNumbers(x, y)
                    if (numbers.size == 2) {
                        println("Found it ! $numbers")
                        sum += (numbers.toList()[0] * numbers.toList()[1])
                    }
                }
            }
        }

        return sum
    }

    val testInput = readInput("Day03_sample")
    val sampleResult = part1(testInput)

    println("Test part 1: $sampleResult")
    check(sampleResult == 4361)

    val input = readInput("Day03")
    println("Result part 1: ${part1(input)}")
//
    val testResult2 = part2(testInput)
    println("Test part 2: $testResult2")
    check(testResult2 == 467835)

    println("Result part 2: ${part2(input)}")
}

private fun List<String>.getAdjacentNumbers(x: Int, y: Int): Set<Int> {
    val directions = listOf(
        Pair(-1, -1),
        Pair(-1, 0),
        Pair(-1, 1),
        Pair(0, -1),
        Pair(0, 1),
        Pair(1, -1),
        Pair(1, 0),
        Pair(1, 1),
    )

    val matches = directions
        .map {
            x + it.first to y + it.second
        }
        .filter { (x, y) ->
            this.get(x, y, '.').isDigit()
        }.map { (x, y) ->
            val line = this.get(x)
            val start = line.substring(0, y + 1).reversed().takeWhile { it.isDigit() }.reversed()
            val end = line.substring(y + 1).takeWhile { it.isDigit() }

            val result = (start + end)
            result.toInt()
        }
    return matches.toSet()
}

private fun List<String>.isPartNumber(x: Int, y: Int, currentNumber: String): Boolean {
    val xRange = x - 1..x + 1
    val yRange = (y - currentNumber.length)..(y + 1)

    val isSymbol = xRange.any { x: Int ->
        yRange.any { y: Int ->
            val value = this.get(x, y, '.')
            value != '.' && !value.isDigit()
        }
    }
    return isSymbol
}

private fun List<String>.get(x: Int, y: Int, default: Char): Char {
    return this.getOrNull(x)?.getOrNull(y) ?: default
}

private fun List<String>.isLastDigitOfNumber(x: Int, y: Int): Boolean {
    val next = this[x].getOrNull(y + 1) ?: '.'
    return !next.isDigit()
}

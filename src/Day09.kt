import java.util.logging.Logger


fun main() {
    val logger = Logger.getLogger("")
    fun drillDownToZero(inputLine: List<Long>): MutableList<List<Long>> {
        val sequences = mutableListOf<List<Long>>()
        sequences.add(inputLine.toMutableList())
        while (sequences.last().any { it != 0L }) {
            val sequence = sequences.last().windowed(2, 1)
                .map {
                    it[1] - it[0]
                }.toMutableList()
            sequences.add(sequence)
        }
        return sequences
    }

    fun part1(fileContent: List<String>): Long {
        val result = fileContent
            .map { it.split(" ").map { it.toLong() } }
            .sumOf { inputLine ->
                println("Line: $inputLine")
                val sequences = drillDownToZero(inputLine)
                val reversed = sequences.asReversed()
                val reversedModified = mutableListOf<List<Long>>()

                reversed.forEachIndexed { index, s ->
                    val toAdd = if (index == 0) {
                        0L
                    } else {
                        val value = s.last() + reversedModified[index - 1].last()
                        value
                    }
                    reversedModified.add(index, (s + toAdd))
                }
                println("Line Modified: ${reversedModified.last()}")

                reversedModified.last().last()
            }

        return result
    }

    fun part2(fileContent: List<String>): Long {
        println("Starting PART 2")
        println("Starting PART 2")
        println("Starting PART 2")
        val result = fileContent
            .map { it.split(" ").map { it.toLong() } }
            .sumOf { inputLine ->
                println("Line: $inputLine")
                val sequences = drillDownToZero(inputLine)
                val reversed = sequences.asReversed()
                val reversedModified = mutableListOf<List<Long>>()

                reversed.forEachIndexed { index, s ->
                    val toAdd = if (index == 0) {
                        0L
                    } else {
                        val value = s.first() - reversedModified[index - 1].first()
                        value
                    }
                    reversedModified.add(index, listOf(toAdd) + s)
                }
                println("Line Modified: ${reversedModified.last()}")

                reversedModified.last().first()
            }

        return result
    }

    val testInput = readInput("Day09_sample")
    val input = readInput("Day09")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 114L)
    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
    val testResult2 = part2(testInput)
    println("Test part 2: $testResult2")
    check(testResult2 == 2L)
    println("Result part 2: ${part2(input)}")
}



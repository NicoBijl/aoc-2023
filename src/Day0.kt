import java.util.logging.Logger

fun main() {
    val logger = Logger.getLogger("")

    fun part1(fileContent: List<String>): Int {
        return 0
    }

    fun part2(fileContent: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day0_sample")
    val input = readInput("Day0")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 8)
    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
    val testResult = part2(testInput)
    println("Test part 3: $testResult")
    check(testResult == 8)
    println("Result part 2: ${part2(input)}")
}

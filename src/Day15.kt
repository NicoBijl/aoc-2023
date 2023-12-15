import java.util.logging.Logger

fun main() {
    val logger = Logger.getLogger("")

    fun part1(fileContent: List<String>): Int {
        return fileContent.map {
            var currentValue = 0
            it.toCharArray().onEach {
                currentValue += it.code
                currentValue *= 17
                currentValue %= 256
            }
            currentValue
        }.sumOf { it }
    }

    fun part2(fileContent: List<String>): Int {
        return 0
    }

    val testInput = readText("Day15_sample").split(",") // HASH
    val testInput2 = readText("Day15_sample2").split(",")
    val input = readText("Day15").split(",")

    val sampleResult = part1(testInput)
    val sampleResult2 = part1(testInput2)
    println("Test part 1: $sampleResult")
    println("Test2 part 1: $sampleResult2")
    check(sampleResult == 52)
    check(sampleResult2 == 1320)
    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
//    val testResult = part2(testInput)
//    println("Test part 3: $testResult")
//    check(testResult == 8)
//    println("Result part 2: ${part2(input)}")
}

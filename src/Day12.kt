import java.util.logging.Logger

fun main() {
    val logger = Logger.getLogger("")

    fun isValid(dots: String, blocks: List<Int>): Boolean {
        var current = 0
        val seen = mutableListOf<Int>()
        dots.forEach { c ->
            when (c) {
                '.' -> {
                    seen.takeIf { current > 0 }?.add(current)
                    current = 0
                }

                '#' -> current++
                else -> throw Error("should not happen")
            }
        }
        if (current > 0) {
            seen.add(current)
        }
        println("isValid $dots vs $blocks = ${seen == blocks}")
        return seen == blocks

    }

    fun countSolutions(dots: String, blocks: List<Int>, i: Int): Int {
        if (i == dots.length) {
            // all dots have been processed, check validity
            return if (isValid(dots, blocks)) 1 else 0
        }
        return if (dots[i] == '?') {
            // if if then we check both options: # or . (recursive...)
            countSolutions(dots.substring(0, i) + '#' + dots.substring(i + 1), blocks, i + 1) + countSolutions(dots.substring(0, i) + '.' + dots.substring(i + 1), blocks, i + 1)
        } else {
            countSolutions(dots, blocks, i + 1)
        }
    }

    fun part1(fileContent: List<String>): Int {
        val result = fileContent.map { line ->
            val dots = line.substringBefore(' ')
            val blocks = line.substringAfter(' ').split(',').map { it.toInt() }
            dots to blocks
        }
            .mapIndexed { index, line ->
                countSolutions(line.first, line.second, 0).also {
                    println("Result for line $index == $it")
                }
            }
            .sumOf { it }
        return result
    }

    fun part2(fileContent: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day12_sample")
    val input = readInput("Day12")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 21)
//    val part1Result = part1(input)
//    check(part1Result == 7169)
//    println("Result part 1: $part1Result")

    // Part 2, updated getValue
//    val testResult = part2(testInput)
//    println("Test part 3: $testResult")
//    check(testResult == 8)
//    println("Result part 2: ${part2(input)}")
}

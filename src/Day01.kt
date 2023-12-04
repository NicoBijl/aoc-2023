fun main() {
    val numbers = mapOf(
        "zero" to "z0ero",
        "one" to "o1ne",
        "two" to "t2wo",
        "three" to "t3hree",
        "four" to "f4our",
        "five" to "f5ive",
        "six" to "s6ix",
        "seven" to "s7even",
        "eight" to "e8ight",
        "nine" to "n9ine"
    )

    fun part1(input: List<String>): Int {
        return input.sumOf {
            (it.first { it.isDigit() }.toString() + it.last { it.isDigit() }.toString()).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { line: String ->
                numbers.entries.fold(line){
                    acc, (search, replace) -> acc.replace(search, replace)
                }
            }
            .sumOf {
                (it.first { it.isDigit() }.toString() + it.last { it.isDigit() }.toString()).toInt()
            }
    }

    val testInput = readInput("Day01_test")
    val testResult = part1(testInput)

    println(testResult)
    check(testResult == 142)


    val input = readInput("Day01")
    part1(input).println()


    val testInput2 = readInput("Day01_test2")
    val testResult2 = part2(testInput2)

    println(testResult2)
    check(testResult2 == 281)

    println("part2: ${part2(input)}")
}

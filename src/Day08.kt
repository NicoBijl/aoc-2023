import java.util.logging.Logger


fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)
fun LongArray.lcm() = this.reduce { acc, n -> lcm(acc, n) }

fun main() {
    val logger = Logger.getLogger("")
    fun parse(fileContent: String): Pair<Array<Boolean>, Map<String, Pair<String, String>>> {
        val instructionIsLeft = fileContent.split("\n\n").first().map { it == 'L' }.toTypedArray()
        val pattern = Regex("^(.+) = \\((.+), (.+)\\)$")
        val directions = fileContent.split("\n\n")[1].split("\n").associate {
            val (_, from, left, right) = pattern.matchEntire(it.trim())?.groupValues!!
            from to Pair(left, right)
        }
        return Pair(instructionIsLeft, directions)
    }

    fun part1(fileContent: String): Long {
        val (instructionIsLeft, directions) = parse(fileContent)
        var current = "AAA"
        var steps = 0
        while (current != "ZZZ") {
            val direction = directions[current]!!
            current = if (instructionIsLeft[steps % instructionIsLeft.size]) direction.first else direction.second
            steps += 1
        }

        return steps.toLong()
    }

    fun part2(fileContent: String): Long {
        val (instructionIsLeft, directions) = parse(fileContent)
        var steps = 0
        var current = directions.keys.filter { it.last() == 'A' }
        val minimumSearches = LongArray(current.size) { -1 }
        while (minimumSearches.any { it < 0 }) {
            current = when (instructionIsLeft[steps % instructionIsLeft.size]) {
                true -> current.map { directions[it]?.first ?: "" }
                else -> current.map { directions[it]?.second ?: "" }
            }
            steps++
            current.forEachIndexed { index, node ->
                if (node.last() == 'Z' && minimumSearches[index] < 0) {
                    minimumSearches[index] = steps.toLong()
                }
            }
        }
        return minimumSearches.lcm()
    }

    val testInput = readText("Day08_sample")
    val input = readText("Day08")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 6L)
    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
    val testResult2 = part2(testInput)
    println("Test part 2: $testResult2")
    check(testResult2 == 6L)
    println("Result part 2: ${part2(input)}")
}



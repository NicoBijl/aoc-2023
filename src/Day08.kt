import java.util.logging.Logger

fun main() {
    val logger = Logger.getLogger("")
    fun part1(fileContent: String): Long {

        val instructions = fileContent.split("\n\n").first()
        val pattern = Regex("^(.+) = \\((.+), (.+)\\)$")
        val directions = fileContent.split("\n\n")[1].split("\n").associate {
            val (_, a, b, c) = pattern.matchEntire(it.trim())?.groupValues!!
            a to Pair(b, c)
        }
        var current = "AAA"
        var i = 0
        var steps = 0L
        while (current != "ZZZ") {
            val currentInstruction = instructions[i]
            val direction = directions[current]!!
            current = if (currentInstruction == 'L') direction.first else direction.second
            i = (i + 1) % instructions.length
            steps += 1
        }

        return steps
    }

    fun part2(fileContent: String): Long {
        val instructions = fileContent.split("\n\n").first().toCharArray()
        val pattern = Regex("^(.+) = \\((.+), (.+)\\)$")
        val directions = fileContent.split("\n\n")[1].split("\n").associateTo(HashMap()) {
            val (_, a, b, c) = pattern.matchEntire(it.trim())?.groupValues!!
            a to Pair(b, c)
        }
        val current = directions.filterKeys { it.last() == 'A' }.keys.toMutableList()
        var i = 0
        var steps = 0L
        logger.info("calculating for ${current.count()}")
        while (current.any { it.last() != 'Z' }) {
            val currentInstruction = instructions[i]
            current.replaceAll {
                val direction = directions[it]!!
                if (currentInstruction == 'L') direction.first else direction.second
            }

            i = (i + 1) % instructions.size
            steps += 1
            if ((steps % 1_000_000_000).toInt() == 0) logger.info("Progress $steps")
        }

        return steps
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


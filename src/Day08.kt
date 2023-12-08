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
        val instructionIsLeft = fileContent.split("\n\n").first().map { it == 'L' }.toTypedArray()
        val pattern = Regex("^(.+) = \\((.+), (.+)\\)$")
        val directions = fileContent.split("\n\n")[1].split("\n").associateTo(HashMap()) {
            val (_, a, b, c) = pattern.matchEntire(it.trim())?.groupValues!!
            a to Pair(b, c)
        }
        val translationKey = directions.keys
        val translationValue = directions.values.map { translationKey.indexOf(it.first) to translationKey.indexOf(it.second) }
        val destinationIndexes = translationKey.filter { it.last() == 'Z' }.map { translationKey.indexOf(it) }
        val current = translationKey.filter { it.last() == 'A' }.map { translationKey.indexOf(it) }.toMutableList()
        var i = 0
        var steps = 0L
        logger.info("calculating for ${current.count()}")
        var startTime = System.currentTimeMillis()
        while (destinationIndexes.any { !current.contains(it) }) {
            val currentInstructionIsLeft = instructionIsLeft[i]
            current.replaceAll {
                val direction = translationValue.elementAt(it)
                if (currentInstructionIsLeft) direction.first else direction.second
            }

            i = (i + 1) % instructionIsLeft.size
            steps += 1
            if ((steps % 1_000_000_000).toInt() == 0) {
                val endTime = System.currentTimeMillis()
                logger.info("Progress $steps time: ${(endTime - startTime) / 1000} seconds")
                startTime = System.currentTimeMillis()
            }
        }
        logger.info("Finished after $steps steps")

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


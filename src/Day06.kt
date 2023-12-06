

fun main() {
    fun parse(lines: List<String>): List<Race> {
        val timeNumbers = lines.get(0).dropWhile { !it.isDigit() }.split(" ").map { it.trim() }.filter { !it.isNullOrEmpty() }.map { it.toLong() }
        val distanceNumbers = lines.get(1).dropWhile { !it.isDigit() }.split(" ").map { it.trim() }.filter { !it.isNullOrEmpty() }.map { it.toLong() }
        return (timeNumbers zip distanceNumbers).map { (time, distance) ->
            Race(time, distance)
        }
    }

    fun parse2(lines: List<String>): Race {
        val timeNumbers =
            lines.get(0).dropWhile { !it.isDigit() }.split(" ").map { it.trim() }.filter { !it.isNullOrEmpty() }.reduce { acc, s -> acc + s }.toLong()
        val distanceNumbers =
            lines.get(1).dropWhile { !it.isDigit() }.split(" ").map { it.trim() }.filter { !it.isNullOrEmpty() }.reduce { acc, s -> acc + s }.toLong()
        return Race(timeNumbers, distanceNumbers)
    }

    fun part1(lines: List<String>): Int {
        val races = parse(lines)
        val result = races.map { race ->
            val otherWinningOptions = (0..race.maxTime)
                .filter { chargeTime -> race.beatsRecords(chargeTime) }
                .count()
            otherWinningOptions
        }.reduce { multiplied, winningOptionsCount -> multiplied * winningOptionsCount }
        return result
    }

    fun part2(lines: List<String>): Int {
        val race = parse2(lines)
        val otherWinningOptions = (0..race.maxTime)
            .filter { chargeTime -> race.beatsRecords(chargeTime) }
            .count()

        return otherWinningOptions
    }

    val testInput = readInput("Day06_sample")
    val sampleResult = part1(testInput)

    println("Test part 1: $sampleResult")
    check(sampleResult == 288)

    val input = readInput("Day06")
    println("Result part 1: ${part1(input)}")

    val testResult2 = part2(testInput)
    println("Test part 2: $testResult2")
    check(testResult2 == 71503)
    println("Result part 2: ${part2(input)}")
}

data class Race(val maxTime: Long, val recordDistance: Long)

private fun Race.beatsRecords(chargeTime: Long): Boolean {
    val timeLeft = this.maxTime - chargeTime
    val distanceTraveled = timeLeft * chargeTime
    return this.recordDistance < distanceTraveled
}

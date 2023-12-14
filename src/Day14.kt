import java.util.logging.Logger

fun main() {
    val logger = Logger.getLogger("")

    fun part1(fileContent: Array<CharArray>): Int {
        val grid = fileContent
        val reversedLineIndices = grid.indices.reversed()
        val updatedGrid = reversedLineIndices.map { line ->
            grid[line].mapIndexed { column, c ->
//                println("process $line / $column => $c")
                if (c == 'O') {
                    // search for . in above lines, when not finding . but finding # stop searching, then round rock can't be moved.
                    val lastDotLineNumber = reversedLineIndices
                        .dropWhile { it >= line }
                        .takeWhile { line -> grid[line][column] != '#' }
                        .filter { grid[it][column] == '.' }
                        .lastOrNull()
//                    println("process $line / $column => $c :: moving from $line / $column to $lastDotLineNumber / $column")
                    if (lastDotLineNumber != null) {
                        grid[lastDotLineNumber][column] = c
                        '.'
                    } else {
                        c
                    }
                } else {
                    c
                }
            }.also { updatedLine -> grid[line] = updatedLine.toCharArray() }
        }.reversed()

        var load = updatedGrid.size
        val totalLoad = updatedGrid.mapIndexed { lineIndex, chars ->
            val result = chars.count { it == 'O' } * load
            load -= 1
            result
        }.sumOf { it }

//        println("Updated Grid")
//        updatedGrid.onEach(::println)
        return totalLoad
    }

    fun part2(fileContent: Array<CharArray>): Int {
        return 0
    }

    val testInput = readArrays("Day14_sample")
    val input = readArrays("Day14")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 136)
    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
//    val testResult = part2(testInput)
//    println("Test part 3: $testResult")
//    check(testResult == 8)
//    println("Result part 2: ${part2(input)}")
}

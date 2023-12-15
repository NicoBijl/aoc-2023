import kotlinx.coroutines.runBlocking
import java.util.logging.Logger


fun main() {
    val logger = Logger.getLogger("")

    fun getLoad(updatedGrid: Array<CharArray>): Int {
        var load = updatedGrid.size
        val totalLoad = updatedGrid.mapIndexed { lineIndex, chars ->
            val result = chars.count { it == 'O' } * load
            load -= 1
            result
        }.sumOf { it }
        return totalLoad
    }

    fun findLastDotLineNumber(reversedLineIndices: Array<Int>, grid: Array<CharArray>, line: Int, column: Int, totalLines: Int): Int? {
        var index = 0
        while (index < totalLines) {
            val currentLineIndex = reversedLineIndices.elementAt(index)

            // Check if the current line is less than the given line
            if (currentLineIndex < line) {
                // Check if the character at the current position is not '#'
                if (grid[currentLineIndex][column] != '#') {
                    // Check if the character at the current position is '.'
                    if (grid[currentLineIndex][column] == '.') {
                        return currentLineIndex
                    }
                } else {
                    // Break the loop if a '#' is encountered
                    break
                }
            }

            index++
        }

        return null
    }

    fun tiltNorth(grid: Array<CharArray>, reversedLineIndices: Array<Int>): Array<CharArray> {
        val totalLines = grid.size
        reversedLineIndices.onEach { line ->
            runBlocking {
                val updatedLine = grid[line].mapIndexed { column, c ->
                    if (c == 'O') {
                        val lastDotLineNumber = findLastDotLineNumber(reversedLineIndices, grid, line, column, totalLines)
                        if (lastDotLineNumber != null) {
                            grid[lastDotLineNumber][column] = c
                            '.'
                        } else {
                            c
                        }
                    } else {
                        c
                    }
                }.toCharArray()
                grid[line] = updatedLine
            }
        }.reversed()
        return grid
    }

    fun part1(fileContent: Array<CharArray>): Int {
        val grid = fileContent
        val updatedGrid = tiltNorth(grid, grid.indices.reversed().toList().toTypedArray())
        return getLoad(updatedGrid)
    }

    fun part2(fileContent: Array<CharArray>, totalCycles: Int): Int {
        println("Part 2, $totalCycles")
        val startTime = System.currentTimeMillis()
        var grid = fileContent
        val reversedLineIndices = grid.indices.reversed().toList().toTypedArray()
        for (currentCycle in 0 until totalCycles) {
            for (rotation in 0 until 4) {
                grid = tiltNorth(grid, reversedLineIndices)
                grid = rotateGrid90Degrees(grid)
            }
            if (currentCycle % (totalCycles / 100) == 0 && currentCycle != 0) {
                val currentTime = System.currentTimeMillis()
                val currentProgress = currentCycle / totalCycles
                println("Progress $currentCycle => current progress: $currentProgress % runtime: ${currentTime - startTime}")
            }
            if (currentCycle == 0 || currentCycle == 1 || currentCycle == 2) {
                println("after cycle index: $currentCycle")
                grid.onEach(::println)
            }

        }
        grid.also {
            println("End Grid after $totalCycles cycles ")
            it.onEach(::println)
        }
        return getLoad(grid)
    }

    val testInput = readArrays("Day14_sample")
    val input = readArrays("Day14")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 136)
    val result1 = part1(input)
    check(result1 == 107053)
    println("Result part 1: $result1")

    // Part 2, updated getValue
    val testResult = part2(testInput, 1000000000)
    println("Test part 3: $testResult")
    check(testResult == 64)
//    println("Result part 2: ${part2(input, 1000000000)}")
}
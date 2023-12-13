import java.util.logging.Logger
import kotlin.math.min

fun main() {
    val logger = Logger.getLogger("")

    fun countDifferences(str1: String, str2: String): Int {
        var count = 0
        val maxLength = maxOf(str1.length, str2.length)

        for (i in 0 until maxLength) {
            val char1 = str1.getOrNull(i)
            val char2 = str2.getOrNull(i)

            if (char1 != char2) {
                count++
            }
        }

        return count
    }

    fun getRowCount(group: List<String>, groupIndex: Int, rowIndices: IntRange): Int {
        var rowCount = 0
        group.forEachIndexed { rowIndex, row ->
//            println("Group $groupIndex - Row $rowIndex")
            if (rowIndex == rowIndices.last) {
                println("no point in checking the last row")
                return@forEachIndexed
            }
            val before = 0..rowIndex
            val after = (rowIndex + 1)..rowIndices.last
            val maxRow = min(before.count(), after.count())
            if (maxRow > 0) {
                val linesBefore = before.reversed().take(maxRow).map { group.elementAt(it) }
//                println("linesBefore $linesBefore")
                val linesAfter = after.take(maxRow).map { group.elementAt(it) }
//                println("linesAfter $linesAfter")
//                println("group $groupIndex $group, row : $rowIndex maxRow to compare= $maxRow")

                var differences = 0
                for (index in linesBefore.indices) {
                    differences += countDifferences(linesAfter[index], linesBefore[index])
                }

                if (differences == 1 && rowCount < after.first) {
                    println("group $groupIndex - Found a group of $maxRow, $linesBefore vs $linesAfter")
                    rowCount = after.first
                }
            } else {
                println("maxRow $maxRow")
            }
        }
        return rowCount
    }

    fun rotateGrid90Degrees(grid: List<String>): List<String> {
        val numRows = grid.size
        val numCols = grid[0].length
        val newGrid = MutableList(numCols) { StringBuilder() }

        for (col in 0 until numCols) {
            for (row in numRows - 1 downTo 0) {
                newGrid[col].append(grid[row][col])
            }
        }

        return newGrid.map { it.toString() }.also {
            println("rotated: ")
            it.onEach { println(it) }
        }
    }


    fun part1(fileContent: String): Int {
        val groups = fileContent.split("\n\n").map { it.split('\n') }

        val total = groups.mapIndexed { groupIndex, group: List<String> ->
            val rowIndices = group.indices
            val columnIndices = group.first().indices

            val rowCount = getRowCount(group, groupIndex, rowIndices)
            println("Group $groupIndex - rowCount = $rowCount")
            val columnCount = getRowCount(rotateGrid90Degrees(group), groupIndex, columnIndices)
            println("Group $groupIndex - columnCount = $columnCount")

            val total = (rowCount * 100) + columnCount
            total
        }.sumOf { it }
        return total
    }


    fun part2(fileContent: String): Int {
        return 0
    }

    val testInput = readText("Day13_sample")
    val input = readText("Day13")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 400)
    println("Result part 1: ${part1(input)}") // 18138 is to low, 26317 is to low

// Part 2, updated getValue
//    val testResult = part2(testInput)
//    println("Test part 3: $testResult")
//    check(testResult == 8)
//    println("Result part 2: ${part2(input)}")
}

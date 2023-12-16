import java.util.logging.Logger
import kotlin.math.abs

fun main() {
    val logger = Logger.getLogger("")


    fun part1(fileContent: Array<CharArray>): Long {
        val stage = Stage(fileContent.map { it.map { it.toString() }.toMutableList() }.toMutableList())
        stage.expand()
        stage.replaceHashSymbol()
        val galaxies = stage.getGalaxies()
        val distance = mutableListOf<Distance>()
        galaxies.forEach { (idFrom: Int, from: Vec2) ->
            galaxies.filter { it.key != idFrom }.map { (idTo, to): Map.Entry<Int, Vec2> ->
                distance.add(Distance("$idFrom/$idTo", from, to, stage.calculateDistance(from, to, 1)))
            }
        }
        return distance.sumOf { it.distance } / 2 //distance contains both directions.
    }

    fun part2(fileContent: Array<CharArray>, multiplier: Int): Long {
        val stage = Stage(fileContent.map { it.map { it.toString() }.toMutableList() }.toMutableList())
        stage.replaceHashSymbol()
        stage.expand2()
        val galaxies = stage.getGalaxies()
        val distance = mutableListOf<Distance>()
        galaxies.forEach { (idFrom: Int, from: Vec2) ->
            galaxies.filter { it.key != idFrom }.map { (idTo, to): Map.Entry<Int, Vec2> ->
                distance.add(Distance("$idFrom/$idTo", from, to, stage.calculateDistance(from, to, multiplier)))
            }
        }
        return distance.sumOf { it.distance } / 2 //distance contains both directions.
    }

    val testInput = readArrays("Day11_sample")
    val input = readArrays("Day11")

//    val sampleResult = part1(testInput)
//    println("Test part 1: $sampleResult")
//    check(sampleResult == 374)
//    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
    val testResult = part2(testInput, 10)
    println("Test part 2: $testResult")
    check(testResult == 1030L)
    val testResult2 = part2(testInput, 100)
    println("Test part 2: $testResult2")
    check(testResult2 == 8410L)
    println("Result part 2: ${part2(input, 1000000)}")
}

data class Stage(var grid: MutableList<MutableList<String>>) {
    fun replaceHashSymbol() {
        var locationNumber = 1
        for (rowIndex in grid.indices) {
            for (columnIndex in grid[rowIndex].indices) {
                if (grid[rowIndex][columnIndex] == "#") {
                    grid[rowIndex][columnIndex] = locationNumber.toString()
                    locationNumber++
                }
            }
        }
    }

    fun expand() {
        val updatedRows = grid.map { it.toMutableList() }.toMutableList()

        for (rowIndex in updatedRows.indices.reversed()) {
            if (updatedRows[rowIndex].all { it == "." }) {
                updatedRows.add(rowIndex, MutableList(updatedRows[rowIndex].size) { "." })
            }
        }


        for (columnIndex in updatedRows.first().indices.reversed()) {
            if (updatedRows.all { it[columnIndex] == "." }) {
                for (rowIndex in updatedRows.indices.reversed()) {
                    updatedRows[rowIndex].add(columnIndex, ".")
                }
            }
        }
        updatedRows.onEach { println(it.joinToString("")) }
        grid = updatedRows.map { it.toMutableList() }.toMutableList()
    }

    fun expand2() {
        val updatedRows = grid.map { it.toMutableList() }.toMutableList()

        for (rowIndex in updatedRows.indices.reversed()) {
            if (updatedRows[rowIndex].all { it == "." }) {
                updatedRows.set(rowIndex, MutableList(updatedRows[rowIndex].size) { "~" })
            }
        }


        for (columnIndex in updatedRows.first().indices.reversed()) {
            if (updatedRows.all { it[columnIndex] == "." || it[columnIndex] == "~" }) {
                for (rowIndex in updatedRows.indices.reversed()) {
                    updatedRows[rowIndex][columnIndex] = "~"
                }
            }
        }
        grid = updatedRows.map { it.toMutableList() }.toMutableList()
        println("Expanding 2 - done")
        grid.onEach {
            println(it.joinToString(""))
        }
    }

    fun getGalaxies(): Map<Int, Vec2> {
        return grid.flatMapIndexed { rowIndex: Int, chars: MutableList<String> ->
            chars.mapIndexed { columnIndex, c -> Triple(rowIndex, columnIndex, c) }
        }
            .filter { it.third.toIntOrNull() != null }
            .associate { it.third.toInt() to Vec2(it.first, it.second) }

    }

    fun calculateDistance(from: Vec2, to: Vec2, multiplier: Int): Long {
        val expansionsCount = findIntermediatePoints(from, to).count { it == "~" }
        return abs(from.row - to.row).toLong() + abs(from.column - to.column) - expansionsCount + (expansionsCount * multiplier)
    }


    fun findIntermediatePoints(start: Vec2, end: Vec2): List<String> {
        val result = mutableListOf<Vec2>()
        var current = start
        while (current != end) {
            val next = when {
                current.row < end.row -> current.down()
                current.row > end.row -> current.up()
                current.column < end.column -> current.right()
                current.column > end.column -> current.left()
                else -> {
                    throw Error("Finished?!")
                }
            }
            result.add(next)
            current = next
        }

        return result.map { grid[it.row][it.column] }
    }
}

data class Vec2(val row: Int, val column: Int) {
    fun down() = Vec2(row + 1, column)
    fun up() = Vec2(row - 1, column)
    fun right() = Vec2(row, column + 1)
    fun left() = Vec2(row, column - 1)
}

data class Distance(val key: String, val from: Vec2, val to: Vec2, val distance: Long)

import java.util.logging.Logger
import kotlin.math.abs

fun main() {
    val logger = Logger.getLogger("")


    fun part1(fileContent: Array<CharArray>): Int {
        val world = World(fileContent.map { it.map { it.toString() }.toMutableList() }.toMutableList())
        world.expand()
        world.replaceHashSymbol()
        val galaxies = world.getGalaxies()
        val distance = mutableListOf<Distance>()
        galaxies.forEach { (idFrom: Int, from: Vec2): Map.Entry<Int, Vec2> ->
            galaxies.filter { it.key != idFrom }.map { (idTo, to): Map.Entry<Int, Vec2> ->
                distance.add(Distance("$idFrom/$idTo", from, to, world.calculateDistance(from, to)))
            }
        }
        return distance.sumOf { it.distance } / 2 //distance contains both directions.
    }

    fun part2(fileContent: Array<Array<String>>): Int {
        return 0
    }

    val testInput = readArrays("Day11_sample")
    val input = readArrays("Day11")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 374)
    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
//    val testResult = part2(testInput)
//    println("Test part 3: $testResult")
//    check(testResult == 8)
//    println("Result part 2: ${part2(input)}")
}

data class World(var grid: MutableList<MutableList<String>>) {
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

    fun getGalaxies(): Map<Int, Vec2> {
        return grid.flatMapIndexed { rowIndex: Int, chars: MutableList<String> ->
            chars.mapIndexed { columnIndex, c -> Triple(rowIndex, columnIndex, c) }
        }
            .filter { it.third.toIntOrNull() != null }
            .associate { it.third.toInt() to Vec2(it.first, it.second) }

    }

    fun calculateDistance(from: Vec2, to: Vec2): Int {
        return abs(from.row - to.row) + abs(from.column - to.column)
    }
}

data class Vec2(val row: Int, val column: Int)
data class Distance(val key: String, val from: Vec2, val to: Vec2, val distance: Int)

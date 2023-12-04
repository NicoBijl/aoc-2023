import java.util.*

fun main() {

    fun lineToGame(it: String): Game {
        val index = it.substringBefore(":").replace("Game ", "").trim().toInt()
        val sets = it.substringAfter(":").trim()
            .split(";")
            .map { it.split(",") }
            .map { cubes ->
                cubes.associate {
                    val color = Color.valueOf(it.trim().substringAfter(" ").uppercase(Locale.getDefault()))
                    val amount = it.trim().substringBefore(" ").toInt()
                    color to amount
                }
            }
        return Game(index, sets)
    }k

    fun part1(maxCubes: Map<Color, Int>, input: List<String>): Int {
        return input.map {
            lineToGame(it)
        }.filter {
            !it.isInvalidGame(maxCubes)
        }.sumOf {
            it.index
        }
    }

    fun part2(input: List<String>): Int {
        return input.map { lineToGame(it) }.sumOf { game ->
            Color.entries
                .associateWith { color -> game.sets.map { it.get(color) ?: 0 }.max() }
                .values
                .reduce { acc, input -> acc * input }
        }
    }

    val testInput = readInput("Day02_test")
    val maxCubes = mapOf(Color.RED to 12, Color.GREEN to 13, Color.BLUE to 14)
    val testResult = part1(maxCubes, testInput)

    println("Test part 1: $testResult")
    
    check(testResult == 8)


    val input = readInput("Day02")
    println("Result part 1: ${part1(maxCubes, input)}")

    val testResult2 = part2(testInput)
    println("Test part 2: $testResult2")
    check(testResult2 == 2286)

    println("Result part 2: ${part2(input)}")
}

data class Game(val index: Int, val sets: List<Map<Color, Int>>) {
    fun isInvalidGame(maxCubes: Map<Color, Int>): Boolean {
        return sets.any { set -> set.any {(color, amount) ->
            maxCubes[color]!! < amount
        }}
    }
}

enum class Color {
    BLUE, RED, GREEN
}
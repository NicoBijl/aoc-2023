import Direction.DOWN
import Direction.LEFT
import Direction.RIGHT
import Direction.UP
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

data class World(val grid: Array<CharArray>) {
    private val energized = mutableSetOf<Pair<Direction, Vec2>>()

    tailrec fun move(current: Vec2, direction: Direction) {
        val char = grid.getOrNull(current.row)?.getOrNull(current.column) ?: return
        val added = energized.add(direction to current)
        if (!added) {
            println("was already energized:  ${current.row} / ${current.column}")
            return;
        }
//        println("Now on pos: ${current.row} / ${current.column} char: $char direction: $direction (energized = ${countEnergized()})")

        val next: List<Direction> = when (char) {
            '/' -> listOf(
                when (direction) {
                    LEFT -> DOWN
                    RIGHT -> UP
                    UP -> RIGHT
                    DOWN -> LEFT
                }
            )

            '\\' -> listOf(
                when (direction) {
                    LEFT -> UP
                    RIGHT -> DOWN
                    UP -> LEFT
                    DOWN -> RIGHT
                }
            )

            '|' -> when (direction) {
                LEFT -> listOf(UP, DOWN)
                RIGHT -> listOf(UP, DOWN)
                else -> listOf(direction)
            }

            '-' -> when (direction) {
                UP -> listOf(LEFT, RIGHT)
                DOWN -> listOf(LEFT, RIGHT)
                else -> listOf(direction)
            }

            else -> {
                listOf(direction)
            }
        }

        next.onEach {
            move(current.go(it), it)
        }
    }

    fun countEnergized(): Int = energized.map { it.second }.toSet().count()

}

private fun Vec2.go(direction: Direction): Vec2 {
    return when (direction) {
        LEFT -> this.left()
        RIGHT -> this.right()
        UP -> this.up()
        DOWN -> this.down()
    }
}

fun main() {
    val logger = Logger.getLogger("")


    fun part1(fileContent: Array<CharArray>): Int {
        val world = World(fileContent)
        // count energized
        world.move(Vec2(0, 0), RIGHT)


        return world.countEnergized()
    }

    fun part2(fileContent: Array<CharArray>): Int {
        val startingPoints = (0..fileContent[0].count()).map { Vec2(0, it) }
        runBlocking {
            val mostEnergized = startingPoints.map {
                async {
                    val world = World(fileContent)
                    world.move(it, DOWN)
                    val energized = world.countEnergized()
                    println("Start from $it , most energized = $energized")
                    energized
                }
            }.awaitAll().max()
            println("max items energized = $mostEnergized")
        }
        return 0
    }

    val testInput = readArrays("Day16_sample")
    val input = readArrays("Day16")
//
//    val sampleResult = part1(testInput)
//    println("Test part 1: $sampleResult")
//    check(sampleResult == 46)
//
//    val result1 = part1(input)
//    println("Result part 1: $result1")
//    check(result1 == 7415)

    // Part 2, updated getValue
    val testResult = part2(testInput)
    println("Test part 2: $testResult")
//    check(testResult == 51)
    println("Result part 2: ${part2(input)}")
}

import java.util.logging.Logger

data class Position(val row: Int, val column: Int, var previous: Position?, var pipe: Pipe?) {
    fun up() = Position(row - 1, column, this, null)
    fun down() = Position(row + 1, column, this, null)
    fun left() = Position(row, column - 1, this, null)
    fun right() = Position(row, column + 1, this, null)
}

data class Pipe(val type: Char)

fun main() {
    val logger = Logger.getLogger("")

    fun parse(fileContent: List<String>) = fileContent.map {
        it.toCharArray()
    }.toTypedArray()

    fun calculateDistanceFromStart(startingPosition: Position?, map: Array<CharArray>): MutableMap<Position, Int> {
        val distanceFromStart: MutableMap<Position, Int> = mutableMapOf(startingPosition!! to 0)
        var positionsToCheck = listOf(startingPosition!!)
        var distance = 0
        while (positionsToCheck.isNotEmpty()) {
            positionsToCheck = positionsToCheck.flatMap { current: Position ->
                current.pipe = Pipe(map[current.row][current.column])
                val adjacent = map.findAdjacent(current, distanceFromStart)
                adjacent.onEach {
                    it.previous = current
                    distanceFromStart[it] = distance + 1
                }
                adjacent
            }
            distance += 1
        }
        return distanceFromStart
    }

    fun getStartingPosition(map: Array<CharArray>): Position? {
        var startingPosition: Position? = null;
        map.forEachIndexed { x, chars ->
            chars.forEachIndexed { y, c ->
                if (c == 'S') {
                    startingPosition = Position(x, y, null, null)
                }
            }
        }
        return startingPosition
    }

    fun part1(fileContent: List<String>): Int {
        val map = parse(fileContent)
        val startingPosition: Position? = getStartingPosition(map)
        val distanceFromStart: MutableMap<Position, Int> = calculateDistanceFromStart(startingPosition, map)

        return distanceFromStart.values.max()
    }

    fun part2(fileContent: List<String>): Int {
        val map = parse(fileContent)
        val startingPosition: Position? = getStartingPosition(map)
        val distanceFromStart: MutableMap<Position, Int> = calculateDistanceFromStart(startingPosition, map)
        val loop = distanceFromStart.keys.map { Pair(it.row, it.column) }.toTypedArray()

        val result = map.allPositions()
            .filter { position: Position ->
                distanceFromStart.none { it.key.row == position.row && it.key.column == position.column }
            }.filter {
                val lineCrossings = map.getLineCrossings(it, loop)
                lineCrossings % 2 == 1// only items in the loop
            }

        return result.size
    }

    val testInput = readInput("Day10_sample")
    val testInput2 = readInput("Day10_sample2")
    val testInput3 = readInput("Day10_sample3")
    val input = readInput("Day10")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 8)
    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
    val testResult2 = part2(testInput2)
    println("Test part 2: $testResult2")
    val testResult3 = part2(testInput3)
    println("Test part 3: $testResult3")
    check(testResult2 == 8)
    check(testResult3 == 10)
    println("Result part 2: ${part2(input)}")
}

private fun Array<CharArray>.getHeight(): Int = this.size
private fun Array<CharArray>.getWidth(): Int = this.first().size

private fun Array<CharArray>.getLineCrossings(position: Position, loop: Array<Pair<Int, Int>>): Int {
    val before = this[position.row].take(position.column)
    val direct = before
        .filterIndexed { columnIndex, c ->
            loop.contains(Pair(position.row, columnIndex)) // if part of loop
        }.count { it == '|' }
    val indirect = before
        .filterIndexed { columnIndex, c ->
            loop.contains(Pair(position.row, columnIndex)) // if part of loop
        }
        .joinToString("")
        .run {
            val matches = Regex("F-*J").findAll(this).count() + Regex("L-*7").findAll(this).count()
            matches
        }
    return direct + indirect
}

private fun Array<CharArray>.allPositions(): List<Position> {
    return this.flatMapIndexed { rowIndex: Int, row: CharArray ->
        row.mapIndexed { columnIndex, cell ->
            Position(rowIndex, columnIndex, null, null)
        }
    }
}

private fun Array<CharArray>.getChar(row: Int, column: Int): Char? {
    return this.getOrNull(row)?.getOrNull(column)
}

private fun Array<CharArray>.getChar(position: Position): Char? {
    return this.getOrNull(position.row)?.getOrNull(position.column)
}


private fun Array<CharArray>.findAdjacent(currentPosition: Position, distanceFromStart: MutableMap<Position, Int>): List<Position> {
    val currentPipe = this.getChar(currentPosition)
    if (currentPipe == 'S') {
        // check all directions
        val result = mutableListOf<Position>()
        if (listOf('|', '7', 'F').any { it == this.getChar(currentPosition.up()) }) {
            result.add(currentPosition.up())
        }
        if (listOf('|', 'L', 'J').any { it == this.getChar(currentPosition.down()) }) {
            result.add(currentPosition.down())
        }
        if (listOf('-', 'F', 'L').any { it == this.getChar(currentPosition.left()) }) {
            result.add(currentPosition.left())
        }
        if (listOf('-', '7', 'J').any { it == this.getChar(currentPosition.right()) }) {
            result.add(currentPosition.right())
        }
        check(result.size == 2)
        return result
    } else {
        val adjacentPipe = when (currentPipe) {
            '|' -> if (currentPosition.row > currentPosition.previous!!.row) {
                currentPosition.down()
            } else {
                currentPosition.up()
            }

            '-' -> if (currentPosition.column > currentPosition.previous!!.column) {
                currentPosition.right()
            } else {
                currentPosition.left()
            }

            'L' -> if (currentPosition.column == currentPosition.previous!!.column) {
                currentPosition.right()
            } else {
                currentPosition.up()
            }

            'J' -> if (currentPosition.column == currentPosition.previous!!.column) {
                currentPosition.left()
            } else {
                currentPosition.up()
            }

            '7' -> if (currentPosition.column == currentPosition.previous!!.column) {
                currentPosition.left()
            } else {
                currentPosition.down()
            }

            'F' -> if (currentPosition.column == currentPosition.previous!!.column) {
                currentPosition.right()
            } else {
                currentPosition.down()
            }

            else -> {
                throw Error("unsupported! $currentPipe on position $currentPosition")
            }
        }
        return if (distanceFromStart.any { it.key.row == adjacentPipe.row && it.key.column == adjacentPipe.column }) {
            emptyList()
        } else {
            listOf(adjacentPipe)
        }

    }


}
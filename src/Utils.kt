import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()
fun readArrays(name: String) = Path("src/$name.txt").readLines().map { it.toCharArray()}.toTypedArray()
fun readText(name: String) = Path("src/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

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
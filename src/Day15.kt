import java.util.logging.Logger

data class Lens(val hash: Int, val label: String, val operator: String, val vocalLength: Int)

fun main() {
    val logger = Logger.getLogger("")

    fun toHash(it: String): Int {
        var currentValue = 0
        it.toCharArray().onEach {
            currentValue += it.code
            currentValue *= 17
            currentValue %= 256
        }
        return currentValue
    }

    fun part1(fileContent: List<String>): Int {
        return fileContent.map {
            toHash(it)
        }.sumOf { it }
    }

    fun part2(fileContent: List<String>): Int {
        val regex = """([a-z]+)(=|-)(\d+)?""".toRegex()
        val boxes = Array<MutableList<Lens>>(256, { mutableListOf<Lens>() })
        fileContent.onEach {
            val (_, label, operator, focalLength) = regex.matchEntire(it)!!.groupValues
            val box = toHash(label)
            if (operator == "-") {
                println("remove $label from box $box")
                boxes[box].removeIf { it.label == label }
            } else {
                val lens = Lens(box, label, operator, focalLength.toInt())
                // replace at index or add at end
                val indexOfExisting = boxes[box].indexOfFirst { it.label == label }
                if (indexOfExisting == -1) {
                    println("add $label from box $box, at the end of the box")
                    boxes[box].add(lens)
                } else {
                    println("replace $label from box $box")
                    boxes[box][indexOfExisting] = lens
                }
            }

        }
        val result = boxes.flatMapIndexed { boxIndex, lenses ->
            lenses.mapIndexed { lensIndex, lens ->
                val result = (boxIndex + 1) * (lensIndex + 1) * lens.vocalLength
                println("${lens.label}: ${(boxIndex + 1)} (box $boxIndex) * $lensIndex (first slot) * ${lens.vocalLength} (focal length) = $result")
                result
            }
        }.sumOf { it }
        return result
    }

    val testInput = readText("Day15_sample").split(",") // HASH
    val testInput2 = readText("Day15_sample2").split(",")

    val sampleResult = part1(testInput)
    val sampleResult2 = part1(testInput2)
    println("Test part 1: $sampleResult")
    println("Test2 part 1: $sampleResult2")
    check(sampleResult == 52)
    check(sampleResult2 == 1320)

    val input = readText("Day15").split(",")
    val resultPart1 = part1(input)
    println("Result part 1: $resultPart1")
    check(resultPart1 == 521341)

    // Part 2, updated getValue
    val testResult = part2(testInput2)
    println("Test part 3: $testResult")
    check(testResult == 145)
    println("Result part 2: ${part2(input)}")
}

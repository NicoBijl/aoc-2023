import javax.xml.crypto.Data

fun main() {
    data class Location(val destinationStart: Long, val sourceStart: Long, val range: Long) {
        constructor(parts: List<Long>) : this(parts[0], parts[1], parts[2])

        val sourceEnd get() = sourceStart + range
    }

    data class LocationKey(val source: String, val destination: String)
    data class Data(val seeds: List<Long>, val map: Map<LocationKey, List<Location>>)

    fun parseFileData(fileText: String): Data {
        val seeds = fileText.split("\n\n").first().split(": ")[1].split(" ").map { it.toLong() }
        val maps = fileText.split("\n\n").drop(1).associate { mapStrs ->
            val (source, destination) = mapStrs.split("\n").first().split(" ").first().split("-to-")
            val locations = mapStrs.split("\n").drop(1).map {
                Location(it.split(" ").map { it.toLong() })
            }
            LocationKey(source, destination) to locations
        }
        return Data(seeds, maps)
    }

    fun part1(fileContent: String): Long {
        val data = parseFileData(fileContent)
        val result = data.seeds.map { seed: Long ->
            var currentKey = "seed"
            var currentLocationNumber = seed
            while (currentKey != "location") {
                val (locationKey, locations) = data.map.filter { it.key.source == currentKey }.entries.first()

                val matchingLocation = locations.mapNotNull {
                    if (it.sourceStart <= currentLocationNumber && currentLocationNumber < it.sourceEnd) {
                        val diff = currentLocationNumber - it.sourceStart
                        it.destinationStart + diff
                    } else {
                        null
                    }
                }.firstOrNull()
                currentLocationNumber = matchingLocation ?: currentLocationNumber
                println("finished with $currentKey next up ${locationKey.destination}")
                currentKey = locationKey.destination
            }

            currentLocationNumber
        }.min()
        return result
    }

    val testInput = readText("Day05_sample")
    val input = readText("Day05")

    val sampleResult = part1(testInput)
    println("Test part 1: $sampleResult")
    check(sampleResult == 35L)
    println("Result part 1: ${part1(input)}")

    // Part 2, updated getValue
//    val testResult2 = part2(testInput)
//    println("Test part 2: $testResult2")
//    check(testResult2 == 46)
//    println("Result part 2: ${part2(input)}")
}


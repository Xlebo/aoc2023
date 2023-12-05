package day02

import getOrFetchInputData
import readInput

fun main() {
    data class Show(
        val red: Int,
        val green: Int,
        val blue: Int
    )

    data class Game(
        val id: Int,
        val red: Int,
        val green: Int,
        val blue: Int
    )

    fun part1(input: List<String>): Int {
        return input.asSequence().map { line ->
            val lineSplit = line.split(":")
            val game = Game(lineSplit[0].split(" ")[1].toInt(), 12, 13, 14)
            val shows = lineSplit[1].split(";").map {
                var red = 0
                var green = 0
                var blue = 0
                it.split(",").forEach { cubes ->
                    run {
                        val values = cubes.split(" ")
                        when (values[2]) {
                            "red" -> red = values[1].toInt()
                            "green" -> green = values[1].toInt()
                            "blue" -> blue = values[1].toInt()
                        }
                    }
                }
                Show(red, green, blue)
            }
            mapOf(game to shows)
        }
            .fold(HashMap<Game, List<Show>>()) { map, pair -> map.putAll(pair); map }
            .filter { (game, shows) ->
                var ret = true
                shows.forEach {
                    if (it.blue > game.blue || it.green > game.green || it.red > game.red) {
                        ret = false
                    }
                }
                ret
            }.map { it.key.id }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence().map { line ->
            val lineSplit = line.split(":")
            var red = 0
            var green = 0
            var blue = 0
            lineSplit[1].split(";").map {
                it.split(",").forEach { cubes ->
                    run {
                        val values = cubes.split(" ")
                        when (values[2]) {
                            "red" -> if (values[1].toInt() > red) red = values[1].toInt()
                            "green" -> if (values[1].toInt() > green) green = values[1].toInt()
                            "blue" -> if (values[1].toInt() > blue) blue = values[1].toInt()
                        }
                    }
                }
            }
            Game(lineSplit[0].split(" ")[1].toInt(), red, green, blue)
        }
            .map { it.red * it.green * it.blue }
            .sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test", "day02")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 8) { "Got: $result1" }
    check(result2 == 2286) { "Got: $result2" }

    val input = getOrFetchInputData(2)
    println(part1(input))
    println(part2(input))
}

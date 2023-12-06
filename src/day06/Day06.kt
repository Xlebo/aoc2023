package day06

import getOrFetchInputData
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val (times, distances) = input
            .map {
                it.split(":")[1]
                    .split(" ")
                    .filter { row -> row.isNotEmpty() }
                    .map { number -> number.toInt() }
            }

        return (times.indices).map { time ->
            (0..distances[time]).fold(0) { points, timeHeld ->
                if (timeHeld * (times[time] - timeHeld) > distances[time]) {
                    points + 1
                } else {
                    points
                }
            }
        }
            .filter { it > 0 }
            .fold(0) { current, next ->
                if (current == 0) {
                    next
                } else {
                    current * next
                }
            }
    }

    fun part2(input: List<String>): Long {
        val (time, distance) = input
            .map {
                it.split(":")[1]
                    .split(" ")
                    .filter { row -> row.isNotEmpty() }
                    .joinToString(separator = "")
                    .toLong()
            }

        return (0..time).fold(0L) { points, timeHeld ->
                if (timeHeld * (time - timeHeld) > distance) {
                    points + 1
                } else {
                    points
                }
            }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test", "day06")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 288) { "Got: $result1" }
    check(result2 == 71503L) { "Got: $result2" }

    val input = getOrFetchInputData(6)
    println(part1(input))
    println(part2(input))
}

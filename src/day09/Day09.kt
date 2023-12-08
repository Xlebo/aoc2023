package day09

import getOrFetchInputData
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test", "day05")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 209) { "Got: $result1" }
    check(result2 == 302) { "Got: $result2" }

    val input = getOrFetchInputData(5)
    println(part1(input))
    println(part2(input))
}
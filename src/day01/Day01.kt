package day01

import getOrFetchInputData
import readInput

fun main() {
    fun part1(input: List<String>): Int {
    }

    fun part2(input: List<String>): Int {
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test", "day01")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 24000) { "Got: $result1" }
    check(result2 == 45000) { "Got: $result2" }

//    val input = getOrFetchInputData(1)
//    println(part1(input))
//    println(part2(input))
}
package day09

import getOrFetchInputData
import readInput

fun main() {

    fun List<Int>.extrapolate(): MutableList<Int> = this.windowed(2, 1).map { it[1] - it[0] }.toMutableList()

    fun part1(input: List<String>): Int {
        return input
            .map { row ->
                row.split(" ").map { it.toInt() }.toMutableList()
            }.sumOf { fistRow ->
                val extrapolated = mutableListOf(fistRow)
                while (!extrapolated.last().all { it == 0 }) {
                    extrapolated.add(extrapolated.last().extrapolate())
                }
                (extrapolated.size - 1 downTo 1).forEach {
                    extrapolated[it - 1].add(extrapolated[it - 1].last() + extrapolated[it].last())
                }
                extrapolated[0].last()
            }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { row ->
                row.split(" ").map { it.toInt() }
            }.sumOf { fistRow ->
                val extrapolated = mutableListOf(fistRow)
                while (!extrapolated.last().all { it == 0 }) {
                    extrapolated.add(extrapolated.last().extrapolate())
                }
                (extrapolated.size - 1 downTo 1).forEach {
                    extrapolated[it - 1] = listOf(extrapolated[it - 1][0] - extrapolated[it][0]) + extrapolated[it - 1]
                }
                extrapolated[0].first()
            }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "day09")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 114) { "Got: $result1" }
    check(result2 == 2) { "Got: $result2" }

    val input = getOrFetchInputData(9)
    println(part1(input))
    println(part2(input))
}
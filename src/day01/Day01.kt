package day01

import getOrFetchInputData
import readInput

fun main() {
    val numbersRegex = "one|two|three|four|five|six|seven|eight|nine|[1-9]".toRegex()

    fun toNumber(number: String): Int {
        val ret = number.toIntOrNull()
        if (ret != null)
            return ret
        return when (number) {
            "one" -> 1
            "two" -> 2
            "three" -> 3
            "four" -> 4
            "five" -> 5
            "six" -> 6
            "seven" -> 7
            "eight" -> 8
            "nine" -> 9
            else -> 0
        }
    }

    fun getLastNumber(line: String): Int {
        for (i in line.length - 1 downTo 0) {
            val last = numbersRegex.findAll(line, i).firstOrNull()
            if (last != null) {
                return toNumber(last.value)
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        return input.map { line ->
            line.filter { it.isDigit() }
                .map { it.digitToInt() }
        }.sumOf { (it.firstOrNull() ?: 0) * 10 + (it.lastOrNull() ?: 0) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            toNumber(numbersRegex.find(it)?.value ?: "0") * 10 + getLastNumber(it)
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test", "day01")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 209) { "Got: $result1" }
    check(result2 == 302) { "Got: $result2" }

    val input = getOrFetchInputData(1)
    println(part1(input))
    println(part2(input))
}
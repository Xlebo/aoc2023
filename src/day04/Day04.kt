package day04

import getOrFetchInputData
import readInput

fun main() {
    fun scorePoints(currentPoints: Int, currentNumber: Int, winnings: List<Int>): Int {
        if (currentNumber !in winnings) {
            return currentPoints
        }
        if (currentPoints == 0) {
            return 1
        }
        return currentPoints * 2
    }

    fun part1(input: List<String>): Int {
        return input.map { it.split(":")[1].split("|") }
            .sumOf { row ->
                row[0]
                    .split(" ")
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() }
                    .fold(0.toInt()) { points, number ->
                        scorePoints(
                            points,
                            number,
                            row[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() })
                    }
            }
    }

    fun part2(input: List<String>): Int {
        val pointsMap = mutableMapOf(1 to 1)
        var currentCard = 1
        while (currentCard <= input.size) {
            if (!pointsMap.containsKey(currentCard)) {
                pointsMap[currentCard] = 1
            }
            val winningNumbers = input[currentCard - 1]
                .split("|")[1]
                .split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
            val winCount = input[currentCard - 1]
                .split(":")[1]
                .split("|")[0]
                .split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
                .fold(0.toInt()) { points, number ->
                    if (number in winningNumbers) {
                        points + 1
                    } else {
                        points
                    }
                }
        for (cards in 1..winCount) {
            if (!pointsMap.containsKey(currentCard + cards)) {
                pointsMap[currentCard + cards] = 1
            }
            pointsMap[currentCard + cards] = pointsMap[currentCard]!! + pointsMap[currentCard + cards]!!
        }
        currentCard++;
    }
    return pointsMap.values.sum()
}


// test if implementation meets criteria from the description, like:
val testInput = readInput("Day04_test", "day04")
val result1 = part1(testInput)
val result2 = part2(testInput)
check(result1 == 13) { "Got: $result1" }
check(result2 == 30) { "Got: $result2" }

val input = getOrFetchInputData(4)
println(part1(input))
println(part2(input))
}
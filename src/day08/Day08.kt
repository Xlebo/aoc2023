package day08

import getOrFetchInputData
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val start = "AAA"
        val end = "ZZZ"
        var current = start
        val instructions = input[0]
        val map = input.subList(2, input.size).map { row ->
            row.filter { it != ' ' && it != '(' && it != ')' }
                .split("=")
        }.associate { row ->
            val turns = row[1].split(",")
            row[0] to (turns[0] to turns[1])
        }

        var repeats = 0
        var step = 0
        while (current != end) {
            current = if (instructions[step] == 'R') {
                map[current]!!.second
            } else {
                map[current]!!.first
            }
            step = step.inc()
            if (step == instructions.length) {
                step = 0
                repeats = repeats.inc()
            }
        }
        return repeats * instructions.length + step
    }

    fun findEndLoop(start: String, map: Map<String, Pair<String, String>>, instructions: String): Pair<Int, Int> {
        var step = 0
        var current = start
        var count = 0
        while (!current.endsWith("Z")) {
            current = if (instructions[step] == 'L') {
                map[current]!!.first
            } else {
                map[current]!!.second
            }
            step += 1
            if (step >= instructions.length) {
                count += 1
                step = 0
            }
        }
        val offset = count * instructions.length + step
        val end = current
        val startStep = step
        count = 0

        do {
            current = if (instructions[step] == 'L') {
                map[current]!!.first
            } else {
                map[current]!!.second
            }
            step += 1
            if (step >= instructions.length) {
                count += 1
                step = 0
            }
        } while(current != end)
        return offset to step + count * instructions.length - startStep
    }

    fun part2(input: List<String>): Int {
        val instructions = input[0]
        val map = input.subList(2, input.size).map { row ->
            row.filter { it != ' ' && it != '(' && it != ')' }
                .split("=")
        }.associate { row ->
            val turns = row[1].split(",")
            row[0] to (turns[0] to turns[1])
        }

        val increments = map.keys.filter { it.endsWith("A") }.associateWith { 0 to 0 }.toMutableMap()
        increments.keys.forEach {
            increments[it] = findEndLoop(it, map, instructions)
        }

        // literally just put the printed out numbers to online LCM finder and got the result, expected more complex solution
        println(increments)
        val paths = increments.keys
            .map { it to increments[it]!!.first }
            .sortedBy { o1 -> o1.second}
            .toMutableList()
        while (paths.first().second != paths.last().second) {
            val lowest = paths.first()
            paths.removeFirst()
            paths.add(lowest.first to increments[lowest.first]!!.second + lowest.second)
            paths.sortBy { o1 -> o1.second }
        }
        return paths.first().second
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test", "day08")
//    val result1 = part1(testInput)
    val result2 = part2(testInput)
//    check(result1 == 6) { "Got: $result1" }
    check(result2 == 6) { "Got: $result2" }

    val input = getOrFetchInputData(8)
//    println(part1(input))
    println(part2(input))
}
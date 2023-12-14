package day12

import getOrFetchInputData
import readInput

fun main() {

    val cache = mutableMapOf<Pair<String, List<Int>>, Int>()

    fun fitDamagedToRow(row: String, damaged: List<Int>): Int {
        return cache.getOrPut(row to damaged) {
            if (damaged.isEmpty()) {
                return if (row.contains('#')) 0 else 1
            }
            val minRowLength = damaged.sum() + damaged.size - 1
            if (row.length < minRowLength) {
                return 0
            }

            if (row.length == damaged[0]) {
                return 1
            }

            val popFirstValue = if (!row.startsWith('#')) {
                fitDamagedToRow(row.substring(1), damaged)
            } else {
                0
            }

            val rest = if (row[damaged[0]] == '?') {
                fitDamagedToRow(row.substring(damaged[0] + 1), damaged.subList(1, damaged.size))
            } else {
                0
            }

            return rest + popFirstValue
        }
    }

    fun fitDamagedIntoRow(rows: List<String>, damageds: List<Int>): Int {
        if (rows.isEmpty() && damageds.isEmpty())
            return 1
        if (damageds.isNotEmpty() && rows.isEmpty()) {
            return 0
        }
        if (rows.size == 1) {
            return fitDamagedToRow(rows[0], damageds)
        }
        return (0..damageds.size).sumOf { damaged ->
            val currentPoints = fitDamagedToRow(rows[0], damageds.subList(0, damaged))
            val others = fitDamagedIntoRow(rows.subList(1, rows.size), damageds.subList(damaged, damageds.size))
            currentPoints * others
        }
    }

    fun part1(input: List<String>): Int {
        val parsedInput = input.map { it.split(" ") }
            .map {
                it[0].split(".").filter { row -> row.isNotEmpty() } to it[1].split(",").map { number -> number.toInt() }
            }

        return parsedInput.sumOf {
            fitDamagedIntoRow(it.first, it.second)
        }
    }

    fun part2(input: List<String>): Int {
       // nebudu to dělat, ne bu du!
        return 42
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test", "day12")
    val result1 = part1(testInput)
//    val result2 = part2(testInput)
    check(result1 == 21) { "Got: $result1" }
//    check(result2 == 525152) { "Got: $result2" }

    val input = getOrFetchInputData(12)
    println(part1(input))
//    println(part2(input))
}
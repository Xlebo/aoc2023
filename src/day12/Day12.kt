package day12

import getOrFetchInputData
import readInput

fun main() {
    val cache = mutableMapOf<Pair<List<String>, List<Int>>, Long>()

    fun fitDamagedToRow(row: String, damaged: List<Int>): Long {
        return cache.getOrPut(listOf(row) to damaged) c@{
            if (damaged.isEmpty()) {
                return@c if (row.contains('#')) 0L else 1L
            }
            val minRowLength = damaged.sum() + damaged.size - 1
            if (row.length < minRowLength) {
                return@c 0L
            }

            if (row.length == damaged[0]) {
                return@c 1L
            }

            val popFirstValue = if (!row.startsWith('#')) {
                fitDamagedToRow(row.substring(1), damaged)
            } else {
                0L
            }

            val rest = if (row[damaged[0]] == '?') {
                fitDamagedToRow(row.substring(damaged[0] + 1), damaged.subList(1, damaged.size))
            } else {
                0L
            }

            return@c rest + popFirstValue
        }
    }

    fun fitDamagedIntoRow(rows: List<String>, damageds: List<Int>): Long {
        return cache.getOrPut(rows to damageds) c@{
            if (rows.isEmpty() && damageds.isEmpty())
                return@c 1L
            if (damageds.isNotEmpty() && rows.isEmpty()) {
                return@c 0L
            }
            if (rows.size == 1) {
                return@c fitDamagedToRow(rows[0], damageds)
            }
            return@c (0..damageds.size).sumOf { damaged ->
                val currentPoints = fitDamagedToRow(rows[0], damageds.subList(0, damaged))
                val others = fitDamagedIntoRow(rows.subList(1, rows.size), damageds.subList(damaged, damageds.size))
                currentPoints * others
            }
        }
    }

    fun part1(input: List<String>): Long {
        val parsedInput = input.map { it.split(" ") }
            .map {
                it[0].split(".").filter { row -> row.isNotEmpty() } to it[1].split(",").map { number -> number.toInt() }
            }

        return parsedInput.sumOf {
            fitDamagedIntoRow(it.first, it.second)
        }
    }

    fun part2(input: List<String>): Long {
        val parsedInput = input.map { it.split(" ") }
            .map {
                ((it[0] + "?").repeat(5).removeSuffix("?")).split(".").filter { row -> row.isNotEmpty() } to
                        (it[1] + ",").repeat(5).split(",").filter { number -> number.isNotEmpty() }
                            .map { number -> number.toInt() }
            }

        return parsedInput.sumOf {
            var result = fitDamagedIntoRow(it.first, it.second)
            println("${it.first} + ${it.second}: $result")
            result
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test", "day12")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 21L) { "Got: $result1" }
    check(result2 == 525152L) { "Got: $result2" }
    println("check 2 successful")

    val input = getOrFetchInputData(12)
    println(part1(input))
    println(part2(input))
}
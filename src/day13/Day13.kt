package day13

import getOrFetchInputDataAsString
import readInputAsString

fun main() {

    fun checkMirror(map: List<String>, top: Int): Boolean {
        (top downTo 0).forEach {
            val bot = top + 1 + top - it
            if (bot == map.size) {
                return true
            }
            if (map[it] != map[bot]) {
                return false
            }
        }
        return true
    }

    fun checkSmudgedMirror(map: List<String>, top: Int): Int {
        var smudges = 0
        (top downTo 0).forEach {
            val bot = top + 1 + top - it
            if (bot == map.size) {
                return smudges
            }
            smudges += map[it].indices.map { field -> map[it][field] == map[bot][field] }.count { value -> !value }
            if (smudges > 1) {
                return smudges
            }
        }
        return smudges
    }

    fun getValueOfMap(map: List<String>): Int {
        val mirrors = map.indices.windowed(2, 1)
            .filter { map[it[0]] == map[it[1]] }
            .filter { checkMirror(map, it[0]) }
        if (mirrors.isNotEmpty()) {
            return (mirrors[0][0] + 1) * 100
        }

        val columns = map[0].indices.map {
            map.map { row -> row[it] }
        }.map {
            it.toString()
        }

        val columnMirrors = columns.indices.windowed(2, 1)
            .filter { columns[it[0]] == columns[it[1]] }
            .filter { checkMirror(columns, it[0]) }

        if (columnMirrors.isNotEmpty()) {
            return columnMirrors[0][0] + 1
        }

        throw IllegalStateException("There should be a mirror right? $map")
    }

    fun getValueOfSmudgedMap(map: List<String>): Int {
        val mirrors = (0 until map.size - 1)
            .filter { checkSmudgedMirror(map, it) == 1 }
        if (mirrors.isNotEmpty()) {
            return (mirrors[0] + 1) * 100
        }

        val columns = map[0].indices.map {
            map.map { row -> row[it] }
        }.map {
            it.toString()
        }

        val columnMirrors = (0 until columns.size - 1)
            .filter { checkSmudgedMirror(columns, it) == 1 }
        if (columnMirrors.isNotEmpty()) {
            return columnMirrors[0] + 1
        }

        throw IllegalStateException("There should be a mirror right? $map")
    }

    fun part1(input: String): Int {
        return input.split("\n\n").map { it.split("\n") }.sumOf { getValueOfMap(it) }
    }

    fun part2(input: String): Int {
        return input.split("\n\n").map { it.split("\n").filter { row -> row.isNotEmpty() } }.sumOf { getValueOfSmudgedMap(it) }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("Day13_test", "day13")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 405) { "Got: $result1" }
    check(result2 == 400) { "Got: $result2" }

    val input = getOrFetchInputDataAsString(13)
    println(part1(input))
    println(part2(input))
}
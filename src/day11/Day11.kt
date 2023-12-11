package day11

import getOrFetchInputData
import readInput
import kotlin.math.abs

typealias Position = Pair<Int, Int>

private infix fun Int.toward(to: Int) = if (this > to) this downTo to + 1 else this until to

fun main() {

    fun part1(input: List<String>): Int {
        val builderMap = List(input.size) { StringBuilder() }
        // add columns
        input[0].indices.forEach { column ->
            var addColumn = true
            for (row in input.indices) {
                val char = input[row][column]
                if (char != '.') {
                    addColumn = false
                }
                builderMap[row].append(char)
            }
            if (addColumn) {
                builderMap.forEach { it.append('.') }
            }
        }

        val enlargedMap = mutableListOf<String>()

        // add rows and assemble the map
        input.indices.forEach { row ->
            enlargedMap.add(builderMap[row].toString())
            if (input[row].all { it == '.' }) {
                enlargedMap.add(builderMap[row].toString())
            }
        }

        val galaxies = enlargedMap.indices.map { row ->
            enlargedMap[row].indices.mapNotNull {
                val char = enlargedMap[row][it]
                if (char == '#') {
                    Position(row, it)
                } else {
                    null
                }
            }
        }.flatten()

        return galaxies.indices.sumOf { first ->
            (first + 1 until galaxies.size).sumOf { second ->
                abs(galaxies[first].first - galaxies[second].first) + abs(galaxies[first].second - galaxies[second].second)
            }
        }
    }

    fun Position.distanceTo(other: Position, map: List<String>): Long {
        var sum = 0L
        (this.first toward other.first).forEach {
            sum += if (map[it][this.second] == 'X') {
                1000000L
            } else {
                1L
            }
        }
        (this.second toward other.second).forEach {
            sum += if (map[this.first][it] == 'X') {
                1000000L
            } else {
                1L
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val builderMap = List(input.size) { StringBuilder() }
        // add columns
        input[0].indices.forEach {
            val column = input.map { row -> row[it] }
            if (column.all { sign -> sign == '.' }) {
                builderMap.forEach { row ->
                    row.append('X')
                }
            } else {
                builderMap.indices.forEach { row ->
                    builderMap[row].append(column[row])
                }
            }
        }

        val enlargedMap = mutableListOf<String>()

        // add rows and assemble the map
        input.indices.forEach { row ->
            if (input[row].all { it == '.' || it == 'X' }) {
                enlargedMap.add("X".repeat(builderMap[row].length))
            } else {
                enlargedMap.add(builderMap[row].toString())
            }
        }

        enlargedMap.forEach {
            println(it)
        }

        val galaxies = enlargedMap.indices.map { row ->
            enlargedMap[row].indices.mapNotNull {
                val char = enlargedMap[row][it]
                if (char == '#') {
                    Position(row, it)
                } else {
                    null
                }
            }
        }.flatten()

        return galaxies.indices.sumOf { first ->
            (first + 1 until galaxies.size).sumOf { second ->
                galaxies[first].distanceTo(galaxies[second], enlargedMap)
            }
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test", "day11")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    println(result2)
    check(result1 == 374) { "Got: $result1" }
    val emptySpacePasses = 2 * (result2 / 1000000L)
    val dotPasses = (result2 % 1000)
    check(dotPasses + emptySpacePasses  == 374L) { "Got: ${dotPasses + emptySpacePasses}" }

    val input = getOrFetchInputData(11)
    println(part1(input))
    println(part2(input))
}
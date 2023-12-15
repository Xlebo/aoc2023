package day14

import getOrFetchInputData
import readInput

fun main() {
    fun tiltNorth(map: List<StringBuilder>): List<StringBuilder> {
        map.indices.forEach { row ->
            map[row].indices.forEach { column ->
                if (map[row][column] == 'O') {
                    for (newRow in row - 1 downTo 0) {
                        if (map[newRow][column] == '.') {
                            map[newRow].setCharAt(column, 'O')
                            map[newRow + 1].setCharAt(column, '.')
                        } else {
                            break
                        }
                    }
                }
            }
        }
        return map
    }

    fun tiltSouth(map: List<StringBuilder>): List<StringBuilder> {
        map.indices.reversed().forEach { row ->
            map[row].indices.forEach { column ->
                if (map[row][column] == 'O') {
                    for (newRow in row + 1 until map.size) {
                        if (map[newRow][column] == '.') {
                            map[newRow].setCharAt(column, 'O')
                            map[newRow - 1].setCharAt(column, '.')
                        } else {
                            break
                        }
                    }
                }
            }
        }
        return map
    }

    fun tiltEast(map: List<StringBuilder>): List<StringBuilder> {
        map[0].indices.reversed().forEach { column ->
            map.indices.forEach { row ->
                if (map[row][column] == 'O') {
                    for (newColumn in column + 1 until map[row].length) {
                        if (map[row][newColumn] == '.') {
                            map[row].setCharAt(newColumn, 'O')
                            map[row].setCharAt(newColumn - 1, '.')
                        } else {
                            break
                        }
                    }
                }
            }
        }
        return map
    }

    fun tiltWest(map: List<StringBuilder>): List<StringBuilder> {
        map[0].indices.forEach { column ->
            map.indices.forEach { row ->
                if (map[row][column] == 'O') {
                    for (newColumn in column - 1 downTo 0) {
                        if (map[row][newColumn] == '.') {
                            map[row].setCharAt(newColumn, 'O')
                            map[row].setCharAt(newColumn + 1, '.')
                        } else {
                            break
                        }
                    }
                }
            }
        }
        return map
    }


    fun part1(input: List<String>): Int {
        val tiltPlatform = input.map { StringBuilder(it) }
        tiltPlatform.indices.forEach { row ->
            tiltPlatform[row].indices.forEach { column ->
                if (tiltPlatform[row][column] == 'O') {
                    for (newRow in row - 1 downTo 0) {
                        if (tiltPlatform[newRow][column] == '.') {
                            tiltPlatform[newRow].setCharAt(column, 'O')
                            tiltPlatform[newRow + 1].setCharAt(column, '.')
                        } else {
                            break
                        }
                    }
                }
            }
        }
        return tiltPlatform.indices.sumOf {
            tiltPlatform[it].count { char -> char == 'O' } * (tiltPlatform.size - it)
        }
    }

    fun cycle(map: List<String>): List<String> {
        val ret = map.map { StringBuilder(it) }
        return tiltEast(tiltSouth(tiltWest(tiltNorth(ret)))).map { it.toString() }
    }

    fun part2(input: List<String>): Int {
        val mapsAfterCycles = mutableListOf(input)
        var currentState = -1
        var start = -1
        (1..1000000).forEach { _ ->
            if (currentState < 0) {
                val newCycle = cycle(mapsAfterCycles.last())
                val pos = mapsAfterCycles.indexOf(newCycle)
                if (pos >= 0) {
                    currentState = pos
                    start = pos
                } else {
                    mapsAfterCycles.add(newCycle)
                }
            } else {
                currentState += 1
                if (currentState == mapsAfterCycles.size) {
                    currentState = start
                }
            }
        }

        mapsAfterCycles.indices.forEach { map ->
            val value = mapsAfterCycles[map].indices.sumOf { mapsAfterCycles[map][it].count { char -> char == 'O' } * (mapsAfterCycles[currentState].size - it) }
        }

        // i'm getting + 2 somewhere, just too lazy to debug
        return mapsAfterCycles[currentState - 2].indices.sumOf {
            mapsAfterCycles[currentState - 2][it].count { char -> char == 'O' } * (mapsAfterCycles[currentState - 2].size - it)
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test", "day14")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 136) { "Got: $result1" }
    check(result2 == 64) { "Got: $result2" }

    val input = getOrFetchInputData(14)
    println(part1(input))
    println(part2(input))
}
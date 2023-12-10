package day10

import getOrFetchInputData
import readInput
import java.lang.StringBuilder

typealias Position = Pair<Int, Int>

fun main() {
    val neighbours = listOf(0 to 1, 0 to -1, -1 to 0, 1 to 0)

    val rightConnecting = listOf('-', 'F', 'L', 'S')
    val leftConnecting = listOf('-', 'J', '7', 'S')
    val topConnecting = listOf('|', 'L', 'J', 'S')
    val botConnecting = listOf('|', '7', 'F', 'S')

    fun Char.connectsToLeft(): Boolean = this in leftConnecting
    fun Char.connectsToRight(): Boolean = this in rightConnecting
    fun Char.connectsToTop(): Boolean = this in topConnecting
    fun Char.connectsToBot(): Boolean = this in botConnecting

    fun Position.getNeighbors(map: List<String>): List<Char> {
        return listOf(
            map[this.first].getOrNull(this.second - 1) ?: 'O',
            map[this.first].getOrNull(this.second + 1) ?: 'O',
            map.getOrNull(this.first - 1)?.get(this.second) ?: 'O',
            map.getOrNull(this.first + 1)?.get(this.second) ?: 'O'
        )
    }

    fun List<String>.get(pos: Position): Char {
        return this[pos.first][pos.second]
    }

    fun Char.isConnectedTo(next: Char, direction: Position): Boolean {
        return when (direction) {
            neighbours[0] -> (this.connectsToRight() && next.connectsToLeft())
            neighbours[1] -> (this.connectsToLeft() && next.connectsToRight())
            neighbours[2] -> (this.connectsToTop() && next.connectsToBot())
            neighbours[3] -> (this.connectsToBot() && next.connectsToTop())
            else -> throw IllegalStateException("Invalid direction $direction")
        }
    }

    fun findNextConnection(position: Position, previous: Position, map: List<String>): Position {
        val candidates = neighbours - (previous.first - position.first to previous.second - position.second)
        candidates
            .forEach {
                if (map[position.first][position.second]
                        .isConnectedTo(
                            map.getOrNull(it.first + position.first)?.getOrNull(it.second + position.second) ?: '.',
                            it
                        )
                ) {
                    return it.first + position.first to it.second + position.second
                }
            }
        throw IllegalStateException("No neighbours for $position")
    }

    fun getFirstNeighbours(position: Position, map: List<String>): List<Position> {
        val ret = mutableListOf<Position>()
        neighbours
            .forEach {
                if (map[position.first][position.second]
                        .isConnectedTo(
                            map.getOrNull(it.first + position.first)?.getOrNull(it.second + position.second) ?: '.',
                            it
                        )
                ) {
                    ret.add(it.first + position.first to it.second + position.second)
                }
            }
        return ret
    }

    fun findPath(input: List<String>): List<List<Position>> {
        var start = Position(0, 0)
        for (row in input.indices) {
            val column = input[row].indexOf('S')
            if (column >= 0) {
                start = Pair(row, column)
                break
            }
        }
        val (first, second) = getFirstNeighbours(start, input)
        val firstPath = mutableListOf(start, first)
        val secondPath = mutableListOf(start, second)
        while (firstPath.last() != secondPath.last()) {
            firstPath.add(findNextConnection(firstPath.last(), firstPath[firstPath.size - 2], input))
            secondPath.add(findNextConnection(secondPath.last(), secondPath[secondPath.size - 2], input))
        }
        return listOf(firstPath, secondPath)
    }

    fun part1(input: List<String>): Int {
        return findPath(input).first().size - 1
    }

    fun part2(input: List<String>): Int {
        val path = mutableSetOf<Position>()
        findPath(input).forEach {
            path.addAll(it)
        }
        val bigMap = listOf(input[0].toCharArray().joinToString(separator = ".")).toMutableList()
        (1 until input.size).forEach {
            bigMap.add(".".repeat(bigMap[0].length))
            bigMap.add(input[it].toCharArray().joinToString(separator = "."))
        }

        //cleanup junk
        (0 until bigMap.size step 2).forEach { row ->
            val cleanedRow = StringBuilder(bigMap[row])
            (0 until bigMap[0].length step 2).forEach { column ->
                if (!path.contains(row / 2 to column / 2)) {
                    cleanedRow.setCharAt(column, '.')
                }
            }
            bigMap[row] = cleanedRow.toString()
        }

        var evaluateDots = true
        while (evaluateDots) {
            evaluateDots = false
            for (row in bigMap.indices) {
                val newRow = StringBuilder(bigMap[row])
                for (column in bigMap[row].indices) {
                    val pos = row to column
                    if (bigMap.get(pos) == '.') {
                        // left right top bot
                        val neighbors = pos.getNeighbors(bigMap)
                        if (neighbors[0].connectsToRight() && neighbors[1].connectsToLeft() &&
                            path.contains(row / 2 to (column - 1) / 2) &&
                            path.contains(row / 2 to (column + 1) / 2)
                        ) {
                            newRow.setCharAt(column, '-')
                            continue
                        }
                        if (neighbors[2].connectsToBot() && neighbors[3].connectsToTop() &&
                            path.contains((row - 1) / 2 to column / 2) &&
                            path.contains((row + 1) / 2 to column / 2)
                        ) {
                            newRow.setCharAt(column, '|')
                            continue
                        }
                        if (neighbors.contains('O')) {
                            newRow.setCharAt(column, 'O')
                            evaluateDots = true
                        }
                    }
                }
                bigMap[row] = newRow.toString()
            }
        }
        bigMap.forEach {
            println(it)
        }

        val sm0lMap = (input.indices).map { row ->
            (input[0].indices).map { column ->
                bigMap[row * 2][column * 2]
            }.joinToString(separator = "")
        }
        sm0lMap.forEach {
            println(it)
        }

        return sm0lMap.sumOf { row -> row.count { it == '.' } }
    }


    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day10_test", "day10")
    val result1 = part1(testInput1)
    check(result1 == 8) { "Got: $result1" }
    println("test 1 passed")
    val testInput2 = readInput("Day10_2_test", "day10")
    val result2 = part2(testInput2)
    check(result2 == 10) { "Got: $result2" }
    println("test 2 passed")

    val input = getOrFetchInputData(10)
    println(part1(input))
    println(part2(input))
}
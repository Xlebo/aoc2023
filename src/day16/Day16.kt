package day16

import getOrFetchInputData
import readInput
import java.util.*

typealias Tile = Pair<Int, Int>

enum class Direction(val tile: Tile) {
    UP(Tile(-1, 0)),
    DOWN(Tile(1, 0)),
    LEFT(Tile(0, -1)),
    RIGHT(Tile(0, 1))
}

data class Beam(
    var start: Tile,
    val direction: Direction
)

fun Beam.passes(char: Char): Boolean {
    if (char == '.') {
        return true
    }
    if (char == '-') {
        return direction == Direction.RIGHT || direction == Direction.LEFT
    }
    if (char == '|') {
        return direction == Direction.UP || direction == Direction.DOWN
    }
    return false
}

operator fun Tile.plus(tile: Tile): Tile {
    return Tile(this.first + tile.first, this.second + tile.second)
}

fun List<String>.get(tile: Tile): Char {
    return this.getOrNull(tile.first)?.getOrNull(tile.second) ?: 'X'
}

fun main() {

    fun evaluateNonEmptySpace(beam: Beam, energizedMap: List<List<Boolean>>, char: Char): List<Beam> {
        if (char == 'X')
            return listOf()
        if (char == '\\') {
            val direction = when (beam.direction) {
                Direction.UP -> Direction.LEFT
                Direction.LEFT -> Direction.UP
                Direction.DOWN -> Direction.RIGHT
                Direction.RIGHT -> Direction.DOWN
            }
            return listOf(Beam(beam.start, direction))
        }
        if (char == '/') {
            val direction = when (beam.direction) {
                Direction.UP -> Direction.RIGHT
                Direction.RIGHT -> Direction.UP
                Direction.LEFT -> Direction.DOWN
                Direction.DOWN -> Direction.LEFT
            }
            return listOf(Beam(beam.start, direction))
        }
        return when (beam.direction) {
            Direction.UP, Direction.DOWN -> listOf(Beam(beam.start, Direction.LEFT), Beam(beam.start, Direction.RIGHT))
            Direction.LEFT, Direction.RIGHT -> listOf(Beam(beam.start, Direction.DOWN), Beam(beam.start, Direction.UP))
        }
    }

    fun moveBeam(input: List<String>, energizedMap: List<MutableList<Boolean>>, beam: Beam): List<Beam> {
        val new = Beam(beam.start + beam.direction.tile, beam.direction)
        while(new.passes(input.get(new.start))) {
            energizedMap[new.start.first][new.start.second] = true
            new.start = new.start + new.direction.tile
        }
        val newBeams = evaluateNonEmptySpace(new, energizedMap, input.get(new.start))
        if (newBeams.isNotEmpty()) {
            energizedMap[new.start.first][new.start.second] = true
        }
        return newBeams
    }

    fun part1(input: List<String>, start: Beam = Beam(Tile(0, -1), Direction.RIGHT)): Int {
        val energizedMap = List(input.size) { MutableList(input[0].length) { false } }
        // store already processed beams to prevent infinite loops
        val beamMap = mutableListOf<Beam>()
        val stack = Stack<Beam>()
        stack.push(start)
        while(stack.isNotEmpty()) {
            val currentBeam = stack.pop()
            moveBeam(input, energizedMap, currentBeam).forEach {
                if (!beamMap.contains(it)) {
                    beamMap.add(it)
                    stack.push(it)
                }
            }
        }
        return energizedMap.sumOf { row -> row.count { it } }
    }

    fun part2(input: List<String>): Int {
        return input.indices.map { listOf(Beam(it to -1, Direction.RIGHT), Beam(it to input.size, Direction.LEFT)) }
            .union(input[0].indices.map { listOf(Beam(-1 to it, Direction.DOWN), Beam(input[0].length to it, Direction.UP)) })
            .flatten()
            .maxOf {
                part1(input, it)
            }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test", "day16")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 46) { "Got: $result1" }
    check(result2 == 51) { "Got: $result2" }

    val input = getOrFetchInputData(16)
    println(part1(input))
    println(part2(input))
}
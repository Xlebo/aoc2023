package day15

import getOrFetchInputData
import getOrFetchInputDataAsString
import readInput
import readInputAsString

fun main() {
    fun part1(input: String): Long {
        return input.split(",").sumOf { string ->
            var value = 0L
            string.forEach {
                value += it.code
                value *= 17
                value %= 256
            }
            value
        }
    }

    fun part2(input: String): Long {
        val hashFunctions = mutableMapOf<String, Long>()
        val boxes = mutableMapOf<Long, MutableList<Pair<String, Int>>>()
        for (lens in input.split(",").map { it.split("[-|=]".toRegex()) }.map { it[0] to it[1] }) {
            val hash = hashFunctions.getOrPut(lens.first) { part1(lens.first) }
            val pos = boxes.getOrPut(hash) { mutableListOf() }.indexOfFirst { it.first == lens.first }
            if (lens.second.isEmpty()) { // minus sign
                if (pos >= 0)
                    boxes[hash]!!.removeAt(pos)
            } else {
                if (pos < 0) {
                    boxes[hash]!!.add(lens.first to lens.second.toInt())
                } else {
                    boxes[hash]!![pos] = lens.first to lens.second.toInt()
                }
            }
        }
        return boxes.keys.sumOf { key ->
            boxes[key]!!.indices.sumOf { index ->
                (key + 1) * (index + 1) * boxes[key]!![index].second
            }
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("Day15_test", "day15")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 1320L) { "Got: $result1" }
    check(result2 == 145L) { "Got: $result2" }

    val input = getOrFetchInputDataAsString(15)
    println(part1(input))
    println(part2(input))
}
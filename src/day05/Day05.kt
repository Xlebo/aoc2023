package day05

import getOrFetchInputData
import readInput
import java.lang.IllegalArgumentException

typealias Range = Pair<Long, Long>

fun main() {

    fun applyMapping(
        source: String,
        dest: String,
        sourceValue: Long,
        destValue: Long,
        range: Long,
        seedMap: Map<Long, MutableMap<String, Long>>
    ) {
        seedMap.values.filter { it[source] in sourceValue until sourceValue + range }
            .forEach { it[dest] = destValue + it[source]!! - sourceValue }
    }


    fun parseSeedInfoMap(seedInfoMap: Map<Long, MutableMap<String, Long>>, input: List<String>) {
        var row = 1
        var source = ""
        var dest = ""
        while (row < input.size) {
            if (input[row].isEmpty()) {
                source = ""
                dest = ""
                row++
                continue
            }
            if (source.isEmpty() && dest.isEmpty()) {
                val splitRow = input[row].split(" ")[0].split("-")
                source = splitRow[0]
                dest = splitRow[2]
                seedInfoMap.forEach {
                    // default rule if no mapping is set
                    it.value[dest] = it.value[source]!!
                }
                row++
                continue
            }
            val mapping = input[row].split(" ").map { it.toLong() }
            applyMapping(source, dest, mapping[1], mapping[0], mapping[2], seedInfoMap)
            row++
        }
    }

    fun part1(input: List<String>): Long {
        val seedInfoMap = input[0]
            .split(":")[1]
            .split(" ")
            .filter { it.isNotEmpty() }
            .associate { it.toLong() to mutableMapOf("seed" to it.toLong()) }
        parseSeedInfoMap(seedInfoMap, input)
        return seedInfoMap.values.minOf { it["location"]!! }
    }

    fun Range.contains(l: Long): Boolean {
        return l >= this.first && l < this.first + this.second
    }

    fun Range.contains(r: Range): Boolean {
        return contains(r.first) || contains(r.first + r.second)
    }

    fun Range.applyOverlap(source: Range, destination: Range): List<Range> {
        val ret = mutableListOf<Range>()
        if (this.contains(source.first)) {
            // | before | source -> destination | after |
            if (this.contains(source.first + source.second)) {
                ret.add(Range(destination.first, destination.second))
                ret.add(Range(first, source.first - first))
                ret.add(Range(source.first + source.second, first + second - source.first - source.second))
            // |          |    |         |
            // |----this---dest|
            //            |dest--source--|
            } else {
                ret.add(Range(destination.first, first + second - source.first))
                ret.add(Range(first, source.first - first))
            }
        } else {
            // |            |    |          |
            // |---source----dest|
            //              |dest---this----|
            if (this.contains(source.first + source.second)) {
                ret.add(Range(first + destination.first - source.first, source.first + source.second - first))
                ret.add(Range(source.first + source.second, first + second - source.first - source.second))
            } else {
                // | source | this -> destination | source |
                if (source.first <= first && source.first + source.second >= first + second) {
                    ret.add(Range(destination.first + (first - source.first), second))
                // | this | | source |
                } else {
                    throw IllegalArgumentException("objects do not overlap")
                }
            }
        }
        return ret.filter { it.second != 0L }
    }

    fun getLowestLocation(seeds: MutableList<Range>, input: List<String>): Long {
        var row = 1
        var source = ""
        var dest = ""
        var currentRanges = seeds
        var nextRanges = mutableListOf<Range>()
        val unprocessedRanges = mutableListOf<Range>()
        while (row < input.size) {
            if (input[row].isEmpty()) {
                source = ""
                dest = ""
                nextRanges.addAll(currentRanges)
                currentRanges = nextRanges
                nextRanges = mutableListOf()
                println(currentRanges.minOf { it.first })
                row++
                continue
            }
            if (source.isEmpty() && dest.isEmpty()) {
                val splitRow = input[row].split(" ")[0].split("-")
                source = splitRow[0]
                dest = splitRow[2]
                row++
                continue
            }
            val mapping = input[row].split(" ").map { it.toLong() }
            val sourceRange = Range(mapping[1], mapping[2])
            val destRange = Range(mapping[0], mapping[2])
            while(currentRanges.isNotEmpty()) {
                val range = currentRanges.removeFirst()
                if (range.contains(sourceRange) || sourceRange.contains(range)) {
                    val ranges = range.applyOverlap(sourceRange, destRange)
                    ranges.forEach {
                        if (it.first == 0L) {
                            println("$range with $source to $dest hit zero on $mapping")
                        }
                    }
                    nextRanges.add(ranges[0])
                    (1 until ranges.size).forEach { unprocessedRanges.add(ranges[it]) }
                } else {
                    unprocessedRanges.add(range)
                }
            }
            currentRanges.addAll(unprocessedRanges)
            unprocessedRanges.clear()
            row++
        }
        nextRanges.addAll(currentRanges)
        return nextRanges.minOf { it.first }
    }

    fun part2(input: List<String>): Long {
        val seedInfoMap = input[0]
            .split(":")[1]
            .split(" ")
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
            .windowed(2, 2)
            .map { Range(it[0], it[1]) }
            .toMutableList()
        return getLowestLocation(seedInfoMap, input)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test", "day05")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 35L) { "Got: $result1" }
    check(result2 == 46L) { "Got: $result2" }

    val input = getOrFetchInputData(5)
    println(part1(input))
    println(part2(input))
}

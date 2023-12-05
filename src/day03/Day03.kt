package day03

import getOrFetchInputData
import readInput

fun main() {
    val nonSymbols = ('0'..'9').plus('.')

    fun processCharacter(char: Char, row: Int, column: Int, symbols: List<MutableList<Boolean>>) {
        if (char in nonSymbols) {
            return
        }
        val hasElementsOnLeft = column > 0
        val hasElementsOnRight = column < symbols[0].size - 1
        val hasElementsOnTop = row > 0
        val hasElementsOnBot = row < symbols.size - 1
        if (hasElementsOnRight) {
            symbols[row][column + 1] = true
            if (hasElementsOnTop) {
                symbols[row - 1][column] = true
                symbols[row - 1][column + 1] = true
            }
            if (hasElementsOnBot) {
                symbols[row + 1][column] = true
                symbols[row + 1][column + 1] = true
            }
        }
        if (hasElementsOnLeft) {
            symbols[row][column - 1] = true
            if (hasElementsOnTop) {
                symbols[row - 1][column] = true
                symbols[row - 1][column - 1] = true
            }
            if (hasElementsOnBot) {
                symbols[row + 1][column] = true
                symbols[row + 1][column - 1] = true
            }
        }
    }

    fun isAdjacentToSymbol(row: Int, columnStart: Int, columnEnd: Int, isNextToSymbol: List<List<Boolean>>): Boolean {
        (columnStart until columnEnd).forEach {
            if (isNextToSymbol[row][it]) {
                return true
            }
        }
        return false
    }

    fun part1(input: List<String>): Long {
        val symbolsMap = List(input.size) { MutableList(input[0].length) { false } }
        (input.indices).forEach { x ->
            (0 until input[0].length).forEach { y ->
                processCharacter(input[x][y], x, y, symbolsMap)
            }
        }
        return (input.indices).sumOf { row ->
            var number = 0
            var numberStartIndex = -1
            var lineSum = 0L
            input[row].indices.forEach {
                if (input[row][it].isDigit()) {
                    number = number * 10 + input[row][it].digitToInt()
                    if (numberStartIndex < 0) {
                        numberStartIndex = it
                    }
                } else {
                    if (number != 0) {
                        if (isAdjacentToSymbol(row, numberStartIndex, it, symbolsMap))
                            lineSum += number
                    }
                    number = 0
                    numberStartIndex = -1
                }
            }
            if (numberStartIndex > 0 && isAdjacentToSymbol(row, numberStartIndex, input[row].length, symbolsMap)) {
                lineSum += number
            }
            lineSum
        }
    }

    fun evaluateGearRatio(c: Char, row: Int, column: Int, parsedNumbers: List<List<Int>>): Long {
        if (c != '*')
            return 0L
        val neighbours = mutableListOf<Long>()
        (row - 1..row + 1).forEach { i ->
            for (j in column - 1..column + 1) {
                val number = parsedNumbers.getOrNull(i)?.getOrNull(j)
                if (number != null && number > 0) {
                    neighbours.add(number.toLong())
                    if ((parsedNumbers.getOrNull(i)?.getOrNull(j + 1) ?: -1) > 0) {
                        break;
                    }
                }
            }
        }
        if (neighbours.size == 2)
            return neighbours[0] * neighbours[1]
        return 0L
    }

    fun part2(input: List<String>): Long {
        val parsedNumbers = List(input.size) { MutableList(input[0].length) { -1 } }
        (input.indices).forEach { row ->
            var number = 0
            var numberStartIndex = -1
            input[row].indices.forEach {
                if (input[row][it].isDigit()) {
                    number = number * 10 + input[row][it].digitToInt()
                    if (numberStartIndex < 0) {
                        numberStartIndex = it
                    }
                } else {
                    if (number != 0) {
                        (numberStartIndex until it).forEach { column ->
                            parsedNumbers[row][column] = number
                        }
                    }
                    number = 0
                    numberStartIndex = -1
                }
            }
            if (numberStartIndex > 0) {
                (numberStartIndex until input[row].length).forEach { column ->
                    parsedNumbers[row][column] = number
                }
            }
        }
        return input.indices.sumOf { row ->
            input[row].indices.sumOf { column ->
                evaluateGearRatio(input[row][column], row, column, parsedNumbers)
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test", "day03")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 4361L) { "Got: $result1" }
    check(result2 == 467835L) { "Got: $result2" }

    val input = getOrFetchInputData(3)
    println(part1(input))
    println(part2(input))
}
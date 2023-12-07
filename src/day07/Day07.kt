package day07

import getOrFetchInputData
import readInput

typealias Card = Char
typealias Cards = List<Card>

fun Card.toValue1(): Int {
    return when (this) {
        'T' -> 10
        'J' -> 11
        'Q' -> 12
        'K' -> 13
        'A' -> 14
        else -> this.digitToInt()
    }
}

fun Card.toValue2(): Int {
    return when (this) {
        'T' -> 10
        'J' -> 1
        'Q' -> 11
        'K' -> 12
        'A' -> 13
        else -> this.digitToInt()
    }
}

fun Card.compare1(other: Card): Int {
    return this.toValue1().compareTo(other.toValue1())
}

fun Card.compare2(other: Card): Int {
    return this.toValue2().compareTo(other.toValue2())
}

fun Cards.compare1(other: Cards): Int {
    if (this.size != other.size) {
        return this.size.compareTo(other.size)
    }
    this.indices.forEach {
        if (this[it] != other[it]) {
            return this[it].compare1(other[it])
        }
    }
    return 0
}

fun Cards.compare2(other: Cards): Int {
    if (this.size != other.size) {
        return this.size.compareTo(other.size)
    }
    this.indices.forEach {
        if (this[it] != other[it]) {
            return this[it].compare2(other[it])
        }
    }
    return 0
}

fun Cards.containsSameCardsOtherThan(
    count: Int = 5,
    joker: Boolean,
    evaluateJ: Boolean = true,
    other: Card = '0'
): Int {
    if (this.containsAll(listOf('T', '9', 'J', '8', 'J'))) {
        println()
    }
    var targetCount = count
    if (joker) {
        targetCount -= this.count { it == 'J' }
    }
    if (targetCount <= 1) {
        return 0
    }
    (0..this.size - targetCount).forEach { start ->
        if (this[start] != other && !(!evaluateJ && this[start] == 'J')) {
            var counter = 0
            (start + 1 until this.size).forEach {
                if (this[start] == this[it]) {
                    counter++
                    if (counter == targetCount - 1) {
                        return this.indexOf(this[start])
                    }
                }
            }
        }
    }
    return -1
}

fun Cards.containsSameCards(count: Int, joker: Boolean): Int {
    return containsSameCardsOtherThan(count, joker, !joker)
}

fun evaluateFiveOfKind(hand: Cards, joker: Boolean): Cards {
    val five = hand.containsSameCards(5, joker)
    if (five < 0) {
        return listOf()
    }
    return listOf('6') + hand
}

fun evaluateFourOfKind(hand: Cards, joker: Boolean): Cards {
    val four = hand.containsSameCards(4, joker)
    if (four < 0) {
        return listOf()
    }
    return listOf('5') + hand
}

fun evaluateFullHouse(hand: Cards, joker: Boolean): Cards {
    val three = hand.containsSameCards(3, joker)
    if (three < 0)
        return listOf()
    val two = hand.containsSameCardsOtherThan(2, joker = false, evaluateJ = false, other = hand[three])
    if (two < 0)
        return listOf()
    return listOf('4') + hand
}

fun evaluateThreeOfKind(hand: Cards, joker: Boolean): Cards {
    val three = hand.containsSameCards(3, joker)
    if (three < 0)
        return listOf()
    return listOf('3') + hand
}

fun evaluateTwoPairs(hand: Cards, joker: Boolean): Cards {
    val firstPair = hand.containsSameCards(2, joker)
    if (firstPair < 0)
        return listOf()
    val secondPair = hand.containsSameCardsOtherThan(2, joker = false, evaluateJ = false, other = hand[firstPair])
    if (secondPair < 0)
        return listOf()
    return listOf('2') + hand
}

fun evaluatePair(hand: Cards, joker: Boolean): Cards {
    val pair = hand.containsSameCards(2, joker)
    if (pair < 0) {
        return listOf()
    }
    return listOf('1') + hand
}

fun evaluateHighest(hand: Cards): Cards = listOf('0') + hand

val combinations: List<(Cards, Boolean) -> (Cards)> = listOf({ hand, joker -> evaluateFiveOfKind(hand, joker) },
    { hand, joker -> evaluateFourOfKind(hand, joker) },
    { hand, joker -> evaluateFullHouse(hand, joker) },
    { hand, joker -> evaluateThreeOfKind(hand, joker) },
    { hand, joker -> evaluateTwoPairs(hand, joker) },
    { hand, joker -> evaluatePair(hand, joker) },
    { hand, _ -> evaluateHighest(hand) }
)

class Hand(cards: List<Card>, val bid: Int, joker: Boolean, compare: (Cards, Cards) -> (Int)) {
    val value: List<Card>

    init {
        this.value = combinations.map { it(cards, joker) }
            .sortedWith { o1, o2 -> compare(o1, o2) }
            .first()
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val handsSorted = input.map { it.split(" ") }
            .filter { it.isNotEmpty() }
            .map { Hand(it[0].toList(), it[1].toInt(), false) { c1: Cards, c2: Cards -> -c1.compare1(c2) } }
            .sortedWith { o1, o2 -> o1.value.compare1(o2.value) }

        return (1..handsSorted.size).sumOf {
            val win = handsSorted[it - 1].bid.toLong() * it.toLong()
//            println("${handsSorted[it - 1].value} won: $it * ${handsSorted[it - 1].bid}")
            win
        }
    }

    fun part2(input: List<String>): Long {
        val handsSorted = input.map { it.split(" ") }
            .filter { it.isNotEmpty() }
            .map { Hand(it[0].toList(), it[1].toInt(), true) { c1: Cards, c2: Cards -> -c1.compare2(c2) } }
            .sortedWith { o1, o2 -> o1.value.compare2(o2.value) }

        return (1..handsSorted.size).sumOf {
            val win = handsSorted[it - 1].bid.toLong() * it.toLong()
            if (handsSorted[it - 1].value.contains('J')) println("${handsSorted[it - 1].value} won: $it * ${handsSorted[it - 1].bid}")
            win
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test", "day07")
    val result1 = part1(testInput)
    val result2 = part2(testInput)
    check(result1 == 6440L) { "Got: $result1" }
    check(result2 == 5905L) { "Got: $result2" }

    val input = getOrFetchInputData(7)
//    println(part1(input))
    println(part2(input))
}
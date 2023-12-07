package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

enum class HandType : Comparable<HandType> {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND
}

class CamelHand(private val cards: CharArray, val bid: Int, var jokerWildcard: Boolean = false) : Comparable<CamelHand> {
    private fun getCardValue(c: Char) = mapOf(
        '2' to 2,
        '3' to 3,
        '4' to 4,
        '5' to 5,
        '6' to 6,
        '7' to 7,
        '8' to 8,
        '9' to 9,
        'T' to 10,
        'J' to if (jokerWildcard) 1 else 11,
        'Q' to 12,
        'K' to 13,
        'A' to 14
    ).getOrDefault(c, 0)

    private val handType: HandType
        get() {
            val cardCounts = mutableMapOf<Char, Int>().withDefault { 0 }
            cards.forEach { cardCounts.merge(it, 1, Int::plus) }
            val jokers = if (jokerWildcard) cardCounts.remove('J') ?: 0 else 0

            val sortedCounts = cardCounts.values.sortedDescending()
            val first = sortedCounts.firstOrNull() ?: 0
            val second = sortedCounts.drop(1).firstOrNull() ?: 0

            return when {
                first + jokers == 5 -> HandType.FIVE_OF_A_KIND
                first + jokers == 4 -> HandType.FOUR_OF_A_KIND
                first + jokers == 3 && second == 2 -> HandType.FULL_HOUSE
                first + jokers == 3 -> HandType.THREE_OF_A_KIND
                first == 2 && second == 2 -> HandType.TWO_PAIR
                first + jokers == 2 -> HandType.ONE_PAIR
                else -> HandType.HIGH_CARD
            }
        }

    private val comparisonValues
        get() = sequenceOf(handType.ordinal) + cards.asSequence().map(::getCardValue)

    override fun compareTo(other: CamelHand) = comparisonValues.zip(other.comparisonValues)
        .map { it.first - it.second }
        .filter { it != 0 }
        .firstOrNull() ?: 0

    companion object {
        fun fromString(string: String): CamelHand {
            val (cards, bid) = string.split(' ')
            return CamelHand(cards.toCharArray(), bid.toInt())
        }
    }
}

object Solution07 : Solution<List<CamelHand>>(AOC_YEAR, 7) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(CamelHand::fromString)

    override fun solve(input: List<CamelHand>): PairOf<Int> {
        fun List<CamelHand>.totalWinnings() = this.withIndex().sumOf { (it.index + 1) * it.value.bid }
        val hands = input.toMutableList()
        hands.sort()
        val ans1 = hands.totalWinnings()
        hands.forEach { it.jokerWildcard = true }
        hands.sort()
        val ans2 = hands.totalWinnings()
        return ans1 to ans2
    }
}

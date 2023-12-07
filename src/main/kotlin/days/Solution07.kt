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

class CamelCard(private val cards: CharArray, val bid: Int, var jokerWildcard: Boolean = false) : Comparable<CamelCard> {
    private fun Char.cardValue(jokerWildcard: Boolean = false) = mapOf(
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
    ).getOrDefault(this, 0)

    private fun handType(): HandType {
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
        get() = sequenceOf(handType().ordinal) + cards.asSequence().map { it.cardValue(jokerWildcard) }

    override fun compareTo(other: CamelCard) = comparisonValues.zip(other.comparisonValues)
        .map { it.first - it.second }
        .filter { it != 0 }
        .firstOrNull() ?: 0

    companion object {
        fun fromString(string: String): CamelCard {
            val (cards, bid) = string.split(' ')
            return CamelCard(cards.toCharArray(), bid.toInt())
        }
    }
}

object Solution07 : Solution<List<CamelCard>>(AOC_YEAR, 7) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(CamelCard::fromString)

    override fun solve(input: List<CamelCard>): PairOf<Int> {
        fun List<CamelCard>.totalWinnings() = this.withIndex().sumOf { (it.index + 1) * it.value.bid }
        val hands = input.toMutableList()
        hands.sort()
        val ans1 = hands.totalWinnings()
        hands.forEach { it.jokerWildcard = true }
        hands.sort()
        val ans2 = hands.totalWinnings()
        return ans1 to ans2
    }
}

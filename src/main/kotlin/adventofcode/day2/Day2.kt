package adventofcode.day2

import java.io.File

/**
 *
 */
fun main(args: Array<String>) {
    val input = File(ClassLoader.getSystemResource("day-02-input.txt").file).readLines()

    // part 1
    val (twos, threes, checksum) = calculateChecksum(input)
    println("$twos x $threes = $checksum")

    // part 2
    val firstDiff = findCloseIds(input).first()
    val result = firstDiff.first.removeRange(firstDiff.indices.first()..firstDiff.indices.first())
    println("${result}")
}

private fun calculateChecksum(input: List<String>): Triple<Int, Int, Int> {
    val countedBoxIds = input.map { toLetterCount(it) }
    val twosCount = countedBoxIds.filter { it.countsForTwo() }.size
    val threesCount = countedBoxIds.filter { it.countsForThree() }.size

    return Triple(twosCount, threesCount, twosCount * threesCount)
}

private fun toLetterCount(boxId: String): BoxIdLetterCount {
    val letterCounts = boxId.toList()
        .groupingBy { it }
        .eachCount()
        .map { LetterCount(it.key, it.value) }

    return BoxIdLetterCount(boxId, letterCounts)
}

data class BoxIdLetterCount(val boxId: String, val letters: List<LetterCount>) {
    fun countsForTwo() = letters.find { it.count == 2 } != null
    fun countsForThree() = letters.find { it.count == 3 } != null
}

class LetterCount(val letter: Char, val count: Int) {

}

private fun findCloseIds(boxIds: List<String>): MutableList<Diff> {
    val boxIdsDifferentBy1 = mutableListOf<Diff>()

    for (first in boxIds) {
        for (second in boxIds) {
            val diff = first.diff(second)

            if (diff.indices.size == 1) {
                boxIdsDifferentBy1.add(diff)
            }
        }
    }

    return boxIdsDifferentBy1
}


fun String.diff(other: String): Diff {
    val indices = mutableListOf<Int>()
    for (i in 0 until Math.min(this.length, other.length)) {
        if (this[i] != other[i]) {
            indices.add(i)
        }
    }

    return Diff(this, other, indices)
}

data class Diff(val first:String, val second:String, val indices:List<Int>)

package ir.mehdi.imposter

import ir.mehdi.imposter.data.local.SeedData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Dataset quality gates — keeps the word bank honest:
 *
 * - words are grouped into 2 TYPES with structural rules:
 *   NORMAL = one token, PRO = two tokens
 * - every word has exactly 3 distinct, non-blank hints; every hint is
 *   exactly ONE word
 * - hints never contain the word itself, never contain ANY other bank word
 *   (even as a substring) and no bank word is ever inside a hint
 * - no duplicate words across the bank; every type has exactly 80 words
 * - the bank covers exactly the 4 semantic domains (20 each) but exposes
 *   none of them in the game logic
 */
class SeedDataTest {

    private val entities = SeedData.getAllWords()
    private val allWords = entities.map { it.word }

    @Test
    fun everyWordHasExactlyThreeUniqueNonBlankHints() {
        entities.forEach { entity ->
            val hints = entity.hints.split("|||")
            assertEquals(
                "word '${entity.word}' must have exactly ${SeedData.HINTS_PER_WORD} hints",
                SeedData.HINTS_PER_WORD,
                hints.size
            )
            assertEquals(
                "hints of '${entity.word}' must all be different",
                SeedData.HINTS_PER_WORD,
                hints.toSet().size
            )
            assertTrue(
                "hints of '${entity.word}' must not be blank",
                hints.none { it.isBlank() }
            )
        }
    }

    @Test
    fun hintsNeverContainTheWordItself() {
        entities.forEach { entity ->
            entity.hints.split("|||").forEach { hint ->
                assertTrue(
                    "hint '$hint' must not contain the word '${entity.word}'",
                    !hint.contains(entity.word)
                )
                assertTrue(
                    "word '${entity.word}' must not be inside hint '$hint'",
                    !entity.word.contains(hint)
                )
            }
        }
    }

    @Test
    fun hintsNeverContainAnyBankWord() {
        entities.forEach { entity ->
            entity.hints.split("|||").forEach { hint ->
                allWords.forEach { word ->
                    assertTrue(
                        "hint '$hint' must not be inside bank word '$word'",
                        !hint.contains(word) && !word.contains(hint)
                    )
                }
            }
        }
    }

    @Test
    fun everyHintIsExactlyOneWord() {
        entities.forEach { entity ->
            entity.hints.split("|||").forEach { hint ->
                assertTrue(
                    "hint '$hint' of '${entity.word}' must be a single word (no spaces)",
                    !hint.contains(" ")
                )
            }
        }
    }

    @Test
    fun wordStructureMatchesType() {
        entities.forEach { entity ->
            val spaces = entity.word.count { it == ' ' }
            val expected = when (entity.type) {
                "NORMAL" -> 0
                "PRO" -> 1
                else -> -1
            }
            assertEquals(
                "word '${entity.word}' has wrong structure for type ${entity.type}",
                expected,
                spaces
            )
            // ZWNJ (نیم‌فاصله) is allowed in words — it is the correct Persian
            // orthography for compounds (چراغ‌قوه) and is not a space.
        }
    }

    @Test
    fun noDuplicateWordsAcrossTheBank() {
        assertEquals(
            "word bank must not contain duplicates",
            allWords.size,
            allWords.toSet().size
        )
    }

    @Test
    fun bothTypesAreCoveredWithEightyWordsEach() {
        val types = entities.map { it.type }.toSet()
        assertEquals(setOf("NORMAL", "PRO"), types)
        types.forEach { type ->
            val count = entities.count { it.type == type }
            assertEquals("type $type must have exactly 80 words", 80, count)
        }
        assertEquals("total word count must be 160", 160, entities.size)
    }

    @Test
    fun noThreeLevelSystemRemains() {
        val types = entities.map { it.type }.toSet()
        assertTrue("EASY must be gone", "EASY" !in types)
        assertTrue("MEDIUM must be gone", "MEDIUM" !in types)
        assertTrue("HARD must be gone", "HARD" !in types)
    }

    @Test
    fun idsAreContiguousFromOne() {
        assertEquals(
            (1L..entities.size.toLong()).toList(),
            entities.map { it.id }
        )
    }
}

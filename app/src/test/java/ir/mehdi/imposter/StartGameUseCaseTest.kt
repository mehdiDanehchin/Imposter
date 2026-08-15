package ir.mehdi.imposter

import ir.mehdi.imposter.data.local.SeedData
import ir.mehdi.imposter.domain.model.GameConfig
import ir.mehdi.imposter.domain.model.Word
import ir.mehdi.imposter.domain.model.WordType
import ir.mehdi.imposter.domain.repository.WordRepository
import ir.mehdi.imposter.domain.usecase.GetWordsByTypeUseCase
import ir.mehdi.imposter.domain.usecase.StartGameUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Game-logic tests covering the hint and starting-player requirements:
 *
 * Scenario 1: hints OFF -> no imposter gets a hint.
 * Scenario 2: hints ON + 1 imposter -> exactly one hint.
 * Scenario 3-4: hints ON + 2/3 imposters -> all hints different.
 * Scenario 5: hint assignment order varies across rounds.
 * Scenario 6: starting player varies across rounds.
 * Scenario 7: no player card ever leaks another role's information.
 * Scenario 8: consecutive games produce fresh, independent states.
 * Scenario 9: player/imposter limits (3..9 players, 1..3 imposters) hold.
 */
class StartGameUseCaseTest {

    /** A new use case backed by the REAL seed dataset. */
    private fun newUseCase(): StartGameUseCase {
        val words = SeedData.getAllWords().map {
            Word(it.id, WordType.valueOf(it.type), it.word, it.hints.split("|||"))
        }
        val repository = object : WordRepository {
            override suspend fun getWordsByType(type: WordType): List<Word> =
                words.filter { it.type == type }

            override suspend fun getWordsExcluding(
                type: WordType,
                excludeIds: List<Long>
            ): List<Word> =
                words.filter { it.type == type && it.id !in excludeIds }
        }
        return StartGameUseCase(GetWordsByTypeUseCase(repository))
    }

    private fun config(players: Int, imposters: Int, hints: Boolean) =
        GameConfig(players, imposters, WordType.NORMAL, hints)

    // ── Scenario 1: hints OFF ────────────────────────────────────────

    @Test
    fun `hints off - no imposter receives any hint`() = runBlocking {
        repeat(25) {
            val state = newUseCase()(config(6, 2, hints = false)).getOrThrow()
            val imposters = state.playerCards.filter { it.isImposter }
            assertEquals(2, imposters.size)
            imposters.forEach { card ->
                assertNull("imposter must not get a hint", card.hint)
                assertNull("imposter must not see the word", card.word)
            }
            state.playerCards.filterNot { it.isImposter }.forEach { card ->
                assertEquals(state.selectedWord.word, card.word)
                assertNull(card.hint)
            }
        }
    }

    // ── Scenario 2: hints ON + 1 imposter ────────────────────────────

    @Test
    fun `hints on with one imposter - gets exactly one hint from the word`() = runBlocking {
        repeat(25) {
            val state = newUseCase()(config(4, 1, hints = true)).getOrThrow()
            val imposter = state.playerCards.single { it.isImposter }
            assertTrue("imposter must get a hint", imposter.hint != null)
            assertTrue("hint must belong to the selected word", imposter.hint in state.selectedWord.hints)
            assertNull("imposter must not see the word", imposter.word)
            state.playerCards.filterNot { it.isImposter }.forEach { card ->
                assertNull("citizen must never see a hint", card.hint)
            }
        }
    }

    // ── Scenarios 3-4: hints ON + 2/3 imposters, all unique ──────────

    @Test
    fun `hints on with multiple imposters - every imposter gets a different hint`() = runBlocking {
        listOf(2, 3).forEach { imposterCount ->
            repeat(25) {
                val state = newUseCase()(config(7, imposterCount, hints = true)).getOrThrow()
                val imposterHints = state.playerCards.filter { it.isImposter }.map { it.hint!! }
                assertEquals(imposterCount, imposterHints.size)
                assertEquals(
                    "imposter hints must be unique per round",
                    imposterCount,
                    imposterHints.toSet().size
                )
                assertTrue(
                    "all hints must come from the selected word",
                    imposterHints.all { it in state.selectedWord.hints }
                )
            }
        }
    }

    // ── Scenario 5: hint assignment is randomized per round ──────────

    @Test
    fun `hint assignment order varies across rounds`() = runBlocking {
        val assignments = mutableSetOf<List<String>>()
        repeat(80) {
            val state = newUseCase()(config(5, 2, hints = true)).getOrThrow()
            assignments += state.playerCards.filter { it.isImposter }.map { it.hint!! }
        }
        assertTrue(
            "hints must not always be assigned in the same order (saw $assignments)",
            assignments.size >= 2
        )
    }

    // ── Scenario 6: starting player is randomized per round ──────────

    @Test
    fun `starting player varies across rounds and stays in range`() = runBlocking {
        val starters = mutableSetOf<Int>()
        repeat(60) {
            val state = newUseCase()(config(6, 1, hints = true)).getOrThrow()
            assertTrue(state.startingPlayerIndex in 0 until 6)
            starters += state.startingPlayerIndex
        }
        assertTrue(
            "starting player must not always be the same person (saw $starters)",
            starters.size >= 3
        )
    }

    // ── Scenario 7: no information leaks between cards ───────────────

    @Test
    fun `imposters never see the word and citizens never see hints`() = runBlocking {
        repeat(30) {
            val state = newUseCase()(config(5, 2, hints = true)).getOrThrow()
            state.playerCards.forEach { card ->
                if (card.isImposter) {
                    assertNull("imposter must not see the real word", card.word)
                } else {
                    assertNull("citizen must not see any hint", card.hint)
                    assertEquals(state.selectedWord.word, card.word)
                }
            }
        }
    }

    // ── Scenario 8: consecutive games are independent and fresh ──────

    @Test
    fun `consecutive rounds never reuse stale hint or starter state`() = runBlocking {
        val useCase = newUseCase()
        repeat(10) {
            val first = useCase(config(5, 2, hints = true)).getOrThrow()
            val second = useCase(config(5, 2, hints = true)).getOrThrow()

            // Both states are fully valid on their own.
            listOf(first, second).forEach { state ->
                assertTrue(state.startingPlayerIndex in 0 until 5)
                val hints = state.playerCards.filter { it.isImposter }.map { it.hint!! }
                assertEquals(2, hints.toSet().size)
            }
        }
    }

    // ── Randomness of word, imposters, and validation ────────────────

    @Test
    fun `word selection is random across games`() = runBlocking {
        val seenWords = mutableSetOf<String>()
        repeat(30) {
            val state = newUseCase()(config(4, 1, hints = true)).getOrThrow()
            seenWords += state.selectedWord.word
        }
        assertTrue("word selection must be random (saw $seenWords)", seenWords.size >= 3)
    }

    @Test
    fun `imposter selection is random across games`() = runBlocking {
        val seenSets = mutableSetOf<Set<Int>>()
        repeat(40) {
            val state = newUseCase()(config(5, 2, hints = true)).getOrThrow()
            seenSets += state.playerCards.filter { it.isImposter }.map { it.playerIndex }.toSet()
        }
        assertTrue("imposter selection must be random (saw $seenSets)", seenSets.size >= 2)
    }

    // ── Scenario 9: player / imposter limits hold (3..9, 1..3) ───────

    @Test
    fun `player and imposter limits are enforced`() = runBlocking {
        val useCase = newUseCase()
        // imposters must be fewer than players
        assertTrue(useCase(config(3, 3, hints = true)).isFailure)
        assertTrue(useCase(config(3, 3, hints = false)).isFailure)
        // more than 3 imposters is not a valid config at all
        assertFalse(GameConfig(6, 4, WordType.NORMAL, true).isValid())
        assertFalse(GameConfig(6, 4, WordType.NORMAL, false).isValid())
        // more than 9 players is not a valid config at all
        assertFalse(GameConfig(10, 2, WordType.NORMAL, true).isValid())
        assertFalse(GameConfig(10, 2, WordType.NORMAL, false).isValid())
        // fewer than 3 players is not a valid config at all
        assertFalse(GameConfig(2, 1, WordType.NORMAL, true).isValid())
        // the maximum valid setup must work
        repeat(20) {
            val state = useCase(config(9, 3, hints = true)).getOrThrow()
            assertEquals(9, state.playerCards.size)
            assertEquals(3, state.playerCards.count { it.isImposter })
            val imposterHints = state.playerCards.filter { it.isImposter }.map { it.hint!! }
            assertEquals(3, imposterHints.toSet().size)
        }
    }

    @Test
    fun `every type produces games`() = runBlocking {
        WordType.entries.forEach { type ->
            val state = newUseCase()(
                GameConfig(4, 1, type, hintsEnabled = true)
            ).getOrThrow()
            assertEquals(type, state.selectedWord.type)
            assertTrue(state.selectedWord.hints.size >= GameConfig.MAX_IMPOSTERS)
        }
    }
}
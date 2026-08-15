package ir.mehdi.imposter.domain.usecase

import ir.mehdi.imposter.domain.model.GameConfig
import ir.mehdi.imposter.domain.model.GameState
import ir.mehdi.imposter.domain.model.PlayerCard
import ir.mehdi.imposter.domain.usecase.GetWordsByTypeUseCase
import kotlin.random.Random

class StartGameUseCase(
    private val getWordsByTypeUseCase: GetWordsByTypeUseCase
) {

    /**
     * Creates a fresh game state with fully randomized setup:
     * 1. a random word is picked from the selected type (NORMAL or PRO),
     * 2. imposters are chosen at random,
     * 3. when hints are enabled, every imposter receives a DIFFERENT hint
     *    (hints are pulled from a shuffled pool, never in a fixed order —
     *    imposter 1/2/3 each get their own unique hint, randomized order),
     * 4. the discussion starting player is picked at random, independent of
     *    the player list order.
     */
    suspend operator fun invoke(config: GameConfig): Result<GameState> {
        if (!config.isValid()) {
            return Result.failure(IllegalArgumentException("Invalid game configuration"))
        }

        val words = getWordsByTypeUseCase(config.type)
        if (words.isEmpty()) {
            return Result.failure(IllegalStateException("No words available for the selected type"))
        }

        val selectedWord = words.random()
        getWordsByTypeUseCase.markWordUsed(selectedWord.id)

        if (config.hintsEnabled && !selectedWord.hasEnoughHints) {
            return Result.failure(
                IllegalStateException("Not enough hints available for the imposters")
            )
        }

        val imposterIndices = selectImposterIndices(config.playerCount, config.imposterCount)

        // Shuffled hint pool -> every imposter receives a unique, differently
        // ordered hint in every game.
        val shuffledHints = selectedWord.hints.shuffled()
        var hintCursor = 0

        val playerCards = (0 until config.playerCount).map { index ->
            val isImposter = index in imposterIndices
            PlayerCard(
                playerIndex = index,
                isImposter = isImposter,
                word = if (isImposter) null else selectedWord.word,
                hint = if (isImposter && config.hintsEnabled) {
                    shuffledHints[hintCursor++]
                } else {
                    null
                }
            )
        }

        val gameState = GameState(
            config = config,
            selectedWord = selectedWord,
            playerCards = playerCards,
            startingPlayerIndex = Random.nextInt(config.playerCount)
        )

        return Result.success(gameState)
    }

    private fun selectImposterIndices(playerCount: Int, imposterCount: Int): Set<Int> {
        val indices = (0 until playerCount).toMutableList()
        indices.shuffle()
        return indices.take(imposterCount).toSet()
    }
}
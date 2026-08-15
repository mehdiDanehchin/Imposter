package ir.mehdi.imposter.domain.model

data class GameState(
    val config: GameConfig,
    val selectedWord: Word,
    val playerCards: List<PlayerCard>,
    val startingPlayerIndex: Int,
    val currentPlayerIndex: Int = 0,
    val isGameFinished: Boolean = false
) {
    val totalPlayers: Int get() = config.playerCount

    val isLastPlayer: Boolean get() = currentPlayerIndex >= totalPlayers - 1

    fun advancePlayer(): GameState {
        val nextIndex = currentPlayerIndex + 1
        return copy(
            currentPlayerIndex = nextIndex,
            isGameFinished = nextIndex >= totalPlayers
        )
    }
}
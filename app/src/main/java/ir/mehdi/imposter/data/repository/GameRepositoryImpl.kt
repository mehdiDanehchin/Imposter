package ir.mehdi.imposter.data.repository

import ir.mehdi.imposter.domain.model.GameState
import ir.mehdi.imposter.domain.repository.GameRepository

class GameRepositoryImpl : GameRepository {

    private var currentGame: GameState? = null

    override fun getGameState(): GameState? = currentGame

    override fun saveGameState(state: GameState) {
        currentGame = state
    }

    override fun advancePlayer(): GameState? {
        val state = currentGame ?: return null
        val newState = state.advancePlayer()
        currentGame = newState
        return newState
    }

    override fun clearGame() {
        currentGame = null
    }
}

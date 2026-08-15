package ir.mehdi.imposter.domain.repository

import ir.mehdi.imposter.domain.model.GameState

interface GameRepository {
    fun getGameState(): GameState?
    fun saveGameState(state: GameState)
    fun advancePlayer(): GameState?
    fun clearGame()
}

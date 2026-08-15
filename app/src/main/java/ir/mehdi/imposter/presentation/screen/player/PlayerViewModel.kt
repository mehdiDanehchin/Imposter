package ir.mehdi.imposter.presentation.screen.player

import androidx.lifecycle.ViewModel
import ir.mehdi.imposter.ImposterApp
import ir.mehdi.imposter.domain.model.GameState
import ir.mehdi.imposter.domain.model.PlayerCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlayerUiState(
    val gameState: GameState? = null,
    val isCardRevealed: Boolean = false,
    val gameFinished: Boolean = false
)

class PlayerViewModel : ViewModel() {

    private val gameRepository = ImposterApp.instance.gameRepository

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(gameState = gameRepository.getGameState()) }
    }

    fun revealCard() {
        _uiState.update { it.copy(isCardRevealed = true) }
    }

    fun nextPlayer() {
        val currentGame = _uiState.value.gameState ?: return

        if (currentGame.isLastPlayer) {
            // Keep the game state: the discussion-start screen needs it to
            // announce the starting player. It is cleared when discussion begins.
            _uiState.update { it.copy(gameFinished = true) }
        } else {
            val newState = gameRepository.advancePlayer()
            _uiState.update {
                it.copy(
                    gameState = newState,
                    isCardRevealed = false
                )
            }
        }
    }
}

package ir.mehdi.imposter.presentation.screen.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.mehdi.imposter.ImposterApp
import ir.mehdi.imposter.domain.model.GameConfig
import ir.mehdi.imposter.domain.model.WordType
import ir.mehdi.imposter.domain.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameSetupUiState(
    val playerCount: Int = 4,
    val imposterCount: Int = 1,
    val level: WordType = WordType.NORMAL,
    val hintsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val gameStarted: Boolean = false
)

class GameSetupViewModel : ViewModel() {

    private val startGameUseCase = ImposterApp.instance.startGameUseCase
    private val gameRepository = ImposterApp.instance.gameRepository

    private val _uiState = MutableStateFlow(GameSetupUiState())
    val uiState: StateFlow<GameSetupUiState> = _uiState.asStateFlow()

    private val _createdGameState = MutableStateFlow<GameState?>(null)
    val createdGameState: StateFlow<GameState?> = _createdGameState.asStateFlow()

    fun updatePlayerCount(count: Int) {
        _uiState.update {
            val newCount = count.coerceIn(GameConfig.MIN_PLAYERS, GameConfig.MAX_PLAYERS)
            // The imposters must always stay below the player count.
            val newImposters = it.imposterCount.coerceAtMost(newCount - 1)
            it.copy(playerCount = newCount, imposterCount = newImposters)
        }
    }

    fun updateImposterCount(count: Int) {
        _uiState.update { it.copy(imposterCount = count.coerceIn(1, GameConfig.MAX_IMPOSTERS)) }
    }

    fun updateLevel(level: WordType) {
        _uiState.update { it.copy(level = level) }
    }

    fun toggleHints() {
        _uiState.update { it.copy(hintsEnabled = !it.hintsEnabled) }
    }

    fun startGame() {
        val state = _uiState.value

        if (state.imposterCount >= state.playerCount) {
            _uiState.update { it.copy(error = "تعداد ایمپاستر باید کمتر از تعداد بازیکنان باشد") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val config = GameConfig(
                playerCount = state.playerCount,
                imposterCount = state.imposterCount,
                type = state.level,
                hintsEnabled = state.hintsEnabled
            )

            val result = startGameUseCase(config)

            result.onSuccess { gameState ->
                gameRepository.saveGameState(gameState)
                _createdGameState.value = gameState
                _uiState.update { it.copy(isLoading = false, gameStarted = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message ?: "خطا در شروع بازی") }
            }
        }
    }

    fun resetGameStarted() {
        _uiState.update { it.copy(gameStarted = false) }
    }
}
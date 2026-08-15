package ir.mehdi.imposter.presentation.screen.discussion

import androidx.lifecycle.ViewModel
import ir.mehdi.imposter.ImposterApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DiscussionStartUiState(
    val startingPlayerIndex: Int? = null,
    val playerCount: Int = 0
)

class DiscussionStartViewModel : ViewModel() {

    private val gameRepository = ImposterApp.instance.gameRepository

    private val _uiState = MutableStateFlow(DiscussionStartUiState())
    val uiState: StateFlow<DiscussionStartUiState> = _uiState.asStateFlow()

    init {
        val game = gameRepository.getGameState()
        _uiState.update {
            it.copy(
                startingPlayerIndex = game?.startingPlayerIndex,
                playerCount = game?.totalPlayers ?: 0
            )
        }
    }

    /**
     * The role-reveal phase is over and discussion begins: the current game
     * state is no longer needed and must not leak into the next round.
     */
    fun beginDiscussion() {
        gameRepository.clearGame()
    }
}
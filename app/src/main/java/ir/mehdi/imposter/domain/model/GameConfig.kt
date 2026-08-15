package ir.mehdi.imposter.domain.model

data class GameConfig(
    val playerCount: Int = 4,
    val imposterCount: Int = 1,
    val type: WordType = WordType.NORMAL,
    val hintsEnabled: Boolean = true
) {
    companion object {
        const val MIN_PLAYERS = 3
        const val MAX_PLAYERS = 9
        const val MAX_IMPOSTERS = 3
        val IMPOSTER_OPTIONS = (1..MAX_IMPOSTERS).toList()
    }

    fun isValid(): Boolean {
        return playerCount in MIN_PLAYERS..MAX_PLAYERS &&
                imposterCount in IMPOSTER_OPTIONS &&
                imposterCount < playerCount
    }
}

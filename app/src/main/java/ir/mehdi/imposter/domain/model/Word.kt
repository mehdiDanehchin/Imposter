package ir.mehdi.imposter.domain.model

data class Word(
    val id: Long = 0,
    val type: WordType,
    val word: String,
    val hints: List<String> = emptyList()
) {
    val hasEnoughHints: Boolean get() = hints.size >= GameConfig.MAX_IMPOSTERS
}

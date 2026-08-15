package ir.mehdi.imposter.domain.model

/**
 * A single player's private card.
 *
 * - Citizens get the real [word] and never a hint.
 * - Imposters get no word and, only when hints are enabled, their own unique [hint].
 *
 * A player must never be able to see another player's card data — the UI only
 * renders the card of the current player.
 */
data class PlayerCard(
    val playerIndex: Int,
    val isImposter: Boolean,
    val word: String? = null,
    val hint: String? = null
)
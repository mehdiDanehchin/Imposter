package ir.mehdi.imposter.domain.model

/**
 * Word types. Words are grouped by STRUCTURE, not topic:
 *
 * - NORMAL : one standalone word (e.g. «کتاب»)
 * - PRO    : a natural two-part compound (e.g. «ماشین لباسشویی»)
 */
enum class WordType(val persianName: String) {
    NORMAL("عادی"),
    PRO("حرفه‌ای")
}

package ir.mehdi.imposter.domain.usecase

import ir.mehdi.imposter.domain.model.Word
import ir.mehdi.imposter.domain.model.WordType
import ir.mehdi.imposter.domain.repository.WordRepository

class GetWordsByTypeUseCase(
    private val wordRepository: WordRepository
) {

    private val recentlyUsedWordIds = mutableListOf<Long>()

    suspend operator fun invoke(type: WordType): List<Word> {
        val allWords = wordRepository.getWordsByType(type)

        if (allWords.isEmpty()) return emptyList()

        val availableWords = if (recentlyUsedWordIds.size >= allWords.size) {
            recentlyUsedWordIds.clear()
            allWords
        } else {
            allWords.filter { it.id !in recentlyUsedWordIds }
                .ifEmpty {
                    recentlyUsedWordIds.clear()
                    allWords
                }
        }

        return availableWords
    }

    fun markWordUsed(wordId: Long) {
        recentlyUsedWordIds.add(wordId)
    }

    fun resetRecentlyUsed() {
        recentlyUsedWordIds.clear()
    }
}

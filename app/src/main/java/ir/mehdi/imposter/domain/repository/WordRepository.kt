package ir.mehdi.imposter.domain.repository

import ir.mehdi.imposter.domain.model.Word
import ir.mehdi.imposter.domain.model.WordType

interface WordRepository {
    suspend fun getWordsByType(type: WordType): List<Word>
    suspend fun getWordsExcluding(type: WordType, excludeIds: List<Long>): List<Word>
}

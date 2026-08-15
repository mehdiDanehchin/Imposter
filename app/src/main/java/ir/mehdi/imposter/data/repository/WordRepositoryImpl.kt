package ir.mehdi.imposter.data.repository

import ir.mehdi.imposter.data.local.dao.WordDao
import ir.mehdi.imposter.domain.model.Word
import ir.mehdi.imposter.domain.model.WordType
import ir.mehdi.imposter.domain.repository.WordRepository

class WordRepositoryImpl(
    private val wordDao: WordDao
) : WordRepository {

    override suspend fun getWordsByType(type: WordType): List<Word> {
        return wordDao.getWordsByType(type.name).map { it.toDomain() }
    }

    override suspend fun getWordsExcluding(
        type: WordType,
        excludeIds: List<Long>
    ): List<Word> {
        return wordDao.getWordsExcluding(type.name, excludeIds).map { it.toDomain() }
    }

    private fun ir.mehdi.imposter.data.local.entity.WordEntity.toDomain() = Word(
        id = id,
        type = WordType.valueOf(type),
        word = word,
        hints = hints.split("|||").filter { it.isNotBlank() }
    )
}

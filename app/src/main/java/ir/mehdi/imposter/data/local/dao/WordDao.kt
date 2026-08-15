package ir.mehdi.imposter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.mehdi.imposter.data.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(words: List<WordEntity>)

    @Query("SELECT * FROM words WHERE type = :type")
    suspend fun getWordsByType(type: String): List<WordEntity>

    @Query("SELECT * FROM words WHERE type = :type AND id NOT IN (:excludeIds)")
    suspend fun getWordsExcluding(type: String, excludeIds: List<Long>): List<WordEntity>

    @Query("SELECT COUNT(*) FROM words")
    suspend fun getWordCount(): Int

    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<WordEntity>
}
package ir.mehdi.imposter.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.mehdi.imposter.data.local.dao.WordDao
import ir.mehdi.imposter.data.local.entity.WordEntity

// Version 8: word-type migration — three-level difficulty (EASY/MEDIUM/HARD)
// replaced by a two-type model (NORMAL/PRO); the WordEntity column is now
// `type` instead of `level`. Destructive migration + reseed is intentional here
// because the words table only holds seeded dataset data.
// Version 9: full word-bank refresh — 160 words (80 NORMAL single-token +
// 80 PRO two-token compounds), all hints regenerated; reseeds the new bank.
// Version 10: ZWNJ (نیم‌فاصله) restored in compound words/hints (چراغ‌قوه,
// سفرهماهی) — correct Persian orthography; ZWNJ is not a space so the
// NORMAL/PRO token structure is unchanged; reseeds the bank.
// Version 11: final hint inspection — کوالا hint اسباب‌بازی → درخت.
@Database(entities = [WordEntity::class], version = 11, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "imposter_database"
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }
    }
}

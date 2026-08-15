package ir.mehdi.imposter

import android.app.Application
import ir.mehdi.imposter.data.local.AppDatabase
import ir.mehdi.imposter.data.local.SeedData
import ir.mehdi.imposter.data.repository.GameRepositoryImpl
import ir.mehdi.imposter.data.repository.WordRepositoryImpl
import ir.mehdi.imposter.domain.repository.GameRepository
import ir.mehdi.imposter.domain.repository.WordRepository
import ir.mehdi.imposter.domain.usecase.GetWordsByTypeUseCase
import ir.mehdi.imposter.domain.usecase.StartGameUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ImposterApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var wordRepository: WordRepository
        private set

    lateinit var gameRepository: GameRepository
        private set

    lateinit var getWordsByTypeUseCase: GetWordsByTypeUseCase
        private set

    lateinit var startGameUseCase: StartGameUseCase
        private set

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = AppDatabase.getInstance(this)
        wordRepository = WordRepositoryImpl(database.wordDao())
        gameRepository = GameRepositoryImpl()
        getWordsByTypeUseCase = GetWordsByTypeUseCase(wordRepository)
        startGameUseCase = StartGameUseCase(getWordsByTypeUseCase)

        seedDatabaseIfNeeded()
    }

    private fun seedDatabaseIfNeeded() {
        applicationScope.launch {
            if (database.wordDao().getWordCount() == 0) {
                database.wordDao().insertAll(SeedData.getAllWords())
            }
        }
    }

    companion object {
        lateinit var instance: ImposterApp
            private set
    }
}

package com.amadiyawa.feature_quiz.data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.amadiyawa.feature_quiz.data.datasource.database.QuestionDao
import com.amadiyawa.feature_quiz.data.datasource.database.QuestionDatabase
import com.amadiyawa.feature_quiz.data.repository.PlayerRepositoryImpl
import com.amadiyawa.feature_quiz.data.repository.QuestionRepositoryImpl
import com.amadiyawa.feature_quiz.data.util.loadQuestionList
import com.amadiyawa.feature_quiz.domain.model.toQuestionEntityModel
import com.amadiyawa.feature_quiz.domain.repository.PlayerRepository
import com.amadiyawa.feature_quiz.domain.repository.QuestionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.dsl.module
import timber.log.Timber

internal val dataModule = module {
    single<QuestionRepository> { QuestionRepositoryImpl(get()) }

    single<PlayerRepository> { PlayerRepositoryImpl(get()) }

    single {
        Room.databaseBuilder(
            get(),
            QuestionDatabase::class.java,
            "questions.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                val questionDao: QuestionDao by inject()

                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val questionList = loadQuestionList(get())
                            questionDao.insertQuestions(
                                questionList.map { it.toQuestionEntityModel() }
                            )
                        } catch (e: Exception) {
                            Timber.e(e, "Error initializing database")
                        }
                    }
                }
            })
            .build()
    }

    single { get<QuestionDatabase>().questions() }

    single { get<QuestionDatabase>().playerDao() }
}
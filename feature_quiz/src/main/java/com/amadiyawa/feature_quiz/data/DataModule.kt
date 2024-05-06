package com.amadiyawa.feature_quiz.data

import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amadiyawa.feature_quiz.data.datasource.database.QuestionDao
import com.amadiyawa.feature_quiz.data.datasource.database.QuestionDatabase
import com.amadiyawa.feature_quiz.data.repository.QuestionRepositoryImpl
import com.amadiyawa.feature_quiz.data.util.loadQuestionList
import com.amadiyawa.feature_quiz.domain.model.toQuestionEntityModel
import com.amadiyawa.feature_quiz.domain.repository.QuestionRepository
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import timber.log.Timber

internal val dataModule = module {
    single<QuestionRepository> { QuestionRepositoryImpl(get()) }

    single {
        Room.databaseBuilder(
            get(),
            QuestionDatabase::class.java,
            "questions.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("QuestionDatabase", "Creating QuestionDatabase")
                    val questionList = loadQuestionList(get())
                    Timber.d("Loaded questions: $questionList")
                    runBlocking {
                        get<QuestionDao>().insertQuestions(
                            questionList.map { it.toQuestionEntityModel() }
                        )
                    }
                }
            })
            .build()
    }

    single { get<QuestionDatabase>().questions() }
}
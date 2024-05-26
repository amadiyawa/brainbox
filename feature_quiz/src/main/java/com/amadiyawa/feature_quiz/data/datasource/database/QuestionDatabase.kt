package com.amadiyawa.feature_quiz.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amadiyawa.feature_quiz.data.datasource.database.model.PlayerEntityModel
import com.amadiyawa.feature_quiz.data.datasource.database.model.QuestionEntityModel

@Database(
    entities = [
        QuestionEntityModel::class,
        PlayerEntityModel::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class QuestionDatabase : RoomDatabase() {
    abstract fun questions(): QuestionDao
    abstract fun playerDao(): PlayerDao
}
package com.amadiyawa.feature_quiz.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amadiyawa.feature_quiz.data.datasource.database.model.QuestionEntityModel

@Dao
internal interface QuestionDao {
    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<QuestionEntityModel>

    @Query("SELECT * FROM questions WHERE level = :level")
    suspend fun getQuestionsByLevel(level: Int): List<QuestionEntityModel>

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getQuestionById(id: Int): QuestionEntityModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntityModel) {
        question.updatedDate = System.currentTimeMillis()
        insertQuestion(question)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questionList: List<QuestionEntityModel>) {
        questionList.forEach { it.updatedDate = System.currentTimeMillis() }
        insertQuestions(questionList)
    }

    @Query("DELETE FROM questions")
    suspend fun deleteAllQuestions()

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestionById(id: Int)

    @Query("DELETE FROM questions WHERE level = :level")
    suspend fun deleteQuestionsByLevel(level: Int)
}
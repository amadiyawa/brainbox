package com.amadiyawa.feature_quiz.domain.repository

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_quiz.data.datasource.database.model.QuestionEntityModel
import com.amadiyawa.feature_quiz.domain.model.Question

internal interface QuestionRepository {
    suspend fun getAllQuestions() : Result<List<Question>>
    suspend fun getQuestionsByLevel(level: Int) : Result<List<Question>>
    suspend fun getQuestionById(id: Int) : Result<Question>
    suspend fun insertQuestions(questionList: List<Question>)
}
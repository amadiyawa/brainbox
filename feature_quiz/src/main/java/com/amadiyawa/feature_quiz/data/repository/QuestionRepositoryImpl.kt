package com.amadiyawa.feature_quiz.data.repository

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_quiz.data.datasource.database.QuestionDao
import com.amadiyawa.feature_quiz.data.datasource.database.model.toQuestion
import com.amadiyawa.feature_quiz.domain.model.Question
import com.amadiyawa.feature_quiz.domain.model.toQuestionEntityModel
import com.amadiyawa.feature_quiz.domain.repository.QuestionRepository

internal class QuestionRepositoryImpl(
    private val questionDao: QuestionDao
): QuestionRepository {
    override suspend fun getAllQuestions(): Result<List<Question>> =
        try {
            val questionList = questionDao.getAllQuestions().map { it.toQuestion() }
            Result.Success(questionList)
        } catch (e: Exception) {
            Result.Failure()
        }

    override suspend fun getQuestionsByLevel(level: Int): Result<List<Question>> =
        try {
            val questionList = questionDao.getQuestionsByLevel(level = level).map {
                it.toQuestion()
            }
            Result.Success(questionList)
        } catch (e: Exception) {
            Result.Failure()
        }

    override suspend fun getQuestionById(id: Int): Result<Question> =
        try {
            val question = questionDao.getQuestionById(id = id).toQuestion()
            Result.Success(question)
        } catch (e: Exception) {
            Result.Failure()
        }

    override suspend fun insertQuestions(questionList: List<Question>) {
        try {
            questionDao.insertQuestions(
                questionList = questionList.map { it.toQuestionEntityModel() }
            )
        } catch (e: Exception) {
            Result.Failure()
        }
    }
}
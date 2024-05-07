package com.amadiyawa.feature_quiz.domain.usecase

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_quiz.domain.model.Question
import com.amadiyawa.feature_quiz.domain.repository.QuestionRepository
import timber.log.Timber

internal class GetQuizUseCase(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(): Result<List<Question>> {
        Timber.d("GetQuestionListUseCase")
        return questionRepository.getAllQuestions()
    }
}
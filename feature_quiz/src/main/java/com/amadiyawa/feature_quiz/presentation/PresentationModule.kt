package com.amadiyawa.feature_quiz.presentation

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.amadiyawa.feature_quiz.presentation.screen.quiz.QuizViewModel


internal val presentationModule = module {
    // QuestionList
    viewModelOf(::QuizViewModel)
}
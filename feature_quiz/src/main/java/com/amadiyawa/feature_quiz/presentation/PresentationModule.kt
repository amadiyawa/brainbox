package com.amadiyawa.feature_quiz.presentation

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.amadiyawa.feature_quiz.presentation.screen.quiz.QuizViewModel
import com.amadiyawa.feature_quiz.presentation.screen.scorelist.ScoreListViewModel


internal val presentationModule = module {
    // QuestionList
    viewModelOf(::QuizViewModel)
    // PlayerList
    viewModelOf(::ScoreListViewModel)
}
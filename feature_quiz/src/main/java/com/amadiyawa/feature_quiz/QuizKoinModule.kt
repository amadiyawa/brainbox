package com.amadiyawa.feature_quiz

import com.amadiyawa.feature_quiz.data.dataModule
import com.amadiyawa.feature_quiz.domain.domainModule
import com.amadiyawa.feature_quiz.presentation.presentationModule

val featureQuizModule = listOf(
    dataModule,
    domainModule,
    presentationModule
)
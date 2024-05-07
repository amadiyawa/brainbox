package com.amadiyawa.feature_quiz.domain


import com.amadiyawa.feature_quiz.domain.usecase.GetQuizUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {
    singleOf(::GetQuizUseCase)
}
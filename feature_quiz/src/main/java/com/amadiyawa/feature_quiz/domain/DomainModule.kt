package com.amadiyawa.feature_quiz.domain


import com.amadiyawa.feature_quiz.domain.usecase.GetQuizUseCase
import com.amadiyawa.feature_quiz.domain.usecase.GetPlayerListUseCase
import com.amadiyawa.feature_quiz.domain.usecase.CreatePlayerUseCase
import com.amadiyawa.feature_quiz.domain.usecase.UpdatePlayerUseCase
import com.amadiyawa.feature_quiz.domain.usecase.GetPlayerByFullNameUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {
    singleOf(::GetQuizUseCase)
    singleOf(::CreatePlayerUseCase)
    singleOf(::GetPlayerListUseCase)
    singleOf(::GetPlayerByFullNameUseCase)
    singleOf(::UpdatePlayerUseCase)
}
package com.amadiyawa.feature_quiz.domain.usecase

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_quiz.domain.model.Player
import com.amadiyawa.feature_quiz.domain.repository.PlayerRepository

internal class GetPlayerByFullNameUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(fullName: String): Result<Player> {
        return playerRepository.getPlayerByFullName(fullName = fullName)
    }
}
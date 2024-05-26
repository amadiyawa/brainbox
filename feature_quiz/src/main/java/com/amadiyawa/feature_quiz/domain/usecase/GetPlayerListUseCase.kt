package com.amadiyawa.feature_quiz.domain.usecase

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_quiz.domain.model.Player
import com.amadiyawa.feature_quiz.domain.repository.PlayerRepository

internal class GetPlayerListUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(): Result<List<Player>> {
        return playerRepository.getAllPlayers()
    }
}
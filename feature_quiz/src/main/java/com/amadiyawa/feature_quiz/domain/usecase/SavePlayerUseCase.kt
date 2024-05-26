package com.amadiyawa.feature_quiz.domain.usecase

import com.amadiyawa.feature_quiz.domain.model.Player
import com.amadiyawa.feature_quiz.domain.repository.PlayerRepository

internal class SavePlayerUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(player: Player) {
        return playerRepository.insertPlayer(player = player)
    }
}
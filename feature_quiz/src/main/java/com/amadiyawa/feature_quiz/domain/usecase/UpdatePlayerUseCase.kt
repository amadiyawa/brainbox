package com.amadiyawa.feature_quiz.domain.usecase

import com.amadiyawa.feature_quiz.domain.model.Score
import com.amadiyawa.feature_quiz.domain.repository.PlayerRepository

internal class UpdatePlayerUseCase(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(
        id: Int,
        scoreList: List<Score>
    ) {
        return playerRepository.updatePlayer(
            id = id,
            scoreList = scoreList
        )
    }
}
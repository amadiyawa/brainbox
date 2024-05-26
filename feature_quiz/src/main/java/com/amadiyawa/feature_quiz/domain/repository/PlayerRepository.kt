package com.amadiyawa.feature_quiz.domain.repository

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_quiz.domain.model.Player

internal interface PlayerRepository {
    suspend fun getAllPlayers(): Result<List<Player>>
    suspend fun getPlayerById(id: Int): Result<Player>
    suspend fun getPlayerByFullName(fullName: String): Result<Player>
    suspend fun insertPlayer(player: Player)
    suspend fun insertPlayers(playerList: List<Player>)
}
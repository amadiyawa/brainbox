package com.amadiyawa.feature_quiz.data.repository

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_quiz.data.datasource.database.PlayerDao
import com.amadiyawa.feature_quiz.data.datasource.database.model.ScoreEntityModel
import com.amadiyawa.feature_quiz.data.datasource.database.model.toPlayer
import com.amadiyawa.feature_quiz.domain.model.Player
import com.amadiyawa.feature_quiz.domain.model.Score
import com.amadiyawa.feature_quiz.domain.model.toPlayerEntityModel
import com.amadiyawa.feature_quiz.domain.model.toScoreEntityModel
import com.amadiyawa.feature_quiz.domain.repository.PlayerRepository

internal class PlayerRepositoryImpl(
    private val playerDao: PlayerDao
) : PlayerRepository {
    override suspend fun getAllPlayers(): Result<List<Player>> =
        try {
            val playerList = playerDao.getAllPlayers().map { it.toPlayer() }
            Result.Success(playerList)
        } catch (e: Exception) {
            Result.Failure()
        }

    override suspend fun getPlayerById(id: Int): Result<Player> =
        try {
            val player = playerDao.getPlayerById(id = id).toPlayer()
            Result.Success(player)
        } catch (e: Exception) {
            Result.Failure()
        }

    override suspend fun getPlayerByFullName(fullName: String): Result<Player> =
        try {
            val player = playerDao.getPlayerByFullName(fullName = fullName).toPlayer()
            Result.Success(player)
        } catch (e: Exception) {
            Result.Failure()
        }

    override suspend fun insertPlayer(player: Player) {
        try {
            playerDao.insertPlayer(player = player.toPlayerEntityModel())
        } catch (e: Exception) {
            Result.Failure()
        }
    }

    override suspend fun updatePlayer(
        id: Int,
        scoreList: List<Score>
    ) {
        try {
            playerDao.updatePlayer(
                id = id,
                scoreList = scoreList.map { it.toScoreEntityModel() }
            )
        } catch (e: Exception) {
            Result.Failure()
        }
    }

    override suspend fun insertPlayers(playerList: List<Player>) {
        try {
            playerDao.insertPlayers(
                playerList = playerList.map { it.toPlayerEntityModel() }
            )
        } catch (e: Exception) {
            Result.Failure()
        }
    }
}
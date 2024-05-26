package com.amadiyawa.feature_quiz.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amadiyawa.feature_quiz.data.datasource.database.model.PlayerEntityModel

@Dao
internal interface PlayerDao {
    @Query("SELECT * FROM players")
    suspend fun getAllPlayers(): List<PlayerEntityModel>

    @Query("SELECT * FROM players WHERE id = :id")
    suspend fun getPlayerById(id: Int): PlayerEntityModel

    @Query("SELECT * FROM players WHERE fullName = :fullName")
    suspend fun getPlayerByFullName(fullName: String): PlayerEntityModel

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    @Query("DELETE FROM players WHERE id = :id")
    suspend fun deletePlayerById(id: Int)

    @Query("DELETE FROM players WHERE fullName = :fullName")
    suspend fun deletePlayerByFullName(fullName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerEntityModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(playerList: List<PlayerEntityModel>)
}
package com.amadiyawa.feature_quiz.data.datasource.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.amadiyawa.feature_quiz.domain.model.Player
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Entity(tableName = "players")
@TypeConverters(ScoresTypeConverter::class)
internal data class PlayerEntityModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val scoreList: List<ScoreEntityModel>
)

internal fun PlayerEntityModel.toPlayer() = Player(
    id = id,
    fullName = fullName,
    scoreList = scoreList.map { it.toPlayerScore() }
)

internal class ScoresTypeConverter {
    @TypeConverter
    fun ScoreListToString(optionList: List<ScoreEntityModel>): String {
        return Json.encodeToString(ListSerializer(ScoreEntityModel.serializer()), optionList)
    }

    @TypeConverter
    fun stringToScoreList(optionList: String): List<ScoreEntityModel> {
        return Json.decodeFromString(ListSerializer(ScoreEntityModel.serializer()), optionList)
    }
}
package com.amadiyawa.feature_quiz.data.datasource.database.model

import com.amadiyawa.feature_quiz.domain.model.Score
import kotlinx.serialization.Serializable

@Serializable
internal data class ScoreEntityModel(
    val point: Int,
    val createdDate: Long = System.currentTimeMillis(),
)

internal fun ScoreEntityModel.toPlayerScore() = Score(
    point = point,
    createdDate = createdDate
)
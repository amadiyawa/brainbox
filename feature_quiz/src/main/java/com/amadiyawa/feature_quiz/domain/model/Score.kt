package com.amadiyawa.feature_quiz.domain.model

import com.amadiyawa.feature_quiz.data.datasource.database.model.ScoreEntityModel

internal data class Score(
    val point: Int,
    val createdDate: Long = System.currentTimeMillis(),
)

internal fun Score.toScoreEntityModel() = ScoreEntityModel(
    point = point,
    createdDate = createdDate
)
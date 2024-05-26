package com.amadiyawa.feature_quiz.domain.model

import com.amadiyawa.feature_quiz.data.datasource.database.model.PlayerEntityModel

internal data class Player(
    val id: Int? = null,
    val fullName: String,
    val scoreList: List<Score>
)

internal fun Player.toPlayerEntityModel() = PlayerEntityModel(
    fullName = fullName,
    scoreList = scoreList.map { it.toScoreEntityModel() }
)
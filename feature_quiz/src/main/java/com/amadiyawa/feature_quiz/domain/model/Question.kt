package com.amadiyawa.feature_quiz.domain.model

import com.amadiyawa.feature_quiz.data.datasource.database.model.QuestionEntityModel
import kotlinx.serialization.Serializable

@Serializable
internal data class Question(
    val id: Int? = null,
    val question: String,
    val optionList: List<String>,
    val answer: String,
    val level: Int,
    val addedDate: Long? = null,
    var updatedDate: Long? = null
)

internal fun Question.toQuestionEntityModel() = QuestionEntityModel(
    question = question,
    optionList = optionList,
    answer = answer,
    level = level
)
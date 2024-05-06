package com.amadiyawa.feature_quiz.data.datasource.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.amadiyawa.feature_quiz.domain.model.Question
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

@Entity(tableName = "questions")
@TypeConverters(OptionsTypeConverter::class)
internal data class QuestionEntityModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val question: String,
    val optionList: List<String>,
    val answer: String,
    val level: Int,
    val addedDate: Long = System.currentTimeMillis(),
    var updatedDate: Long = System.currentTimeMillis()
)

internal fun QuestionEntityModel.toQuestion() = Question(
    id = id,
    question = question,
    optionList = optionList,
    answer = answer,
    level = level,
    addedDate = addedDate,
    updatedDate = updatedDate
)

internal class OptionsTypeConverter{
    @TypeConverter
    fun optionListToString(optionList: List<String>): String {
        return Json.encodeToString(ListSerializer(String.serializer()), optionList)
    }

    @TypeConverter
    fun stringToOptionList(optionList: String): List<String> {
        return Json.decodeFromString(optionList)
    }
}
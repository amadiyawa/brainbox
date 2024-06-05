package com.amadiyawa.feature_quiz.data.util

import android.content.Context
import android.content.res.Resources
import com.amadiyawa.feature_quiz.R
import com.amadiyawa.feature_quiz.domain.model.Question
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

internal fun loadQuestionList(context: Context): List<Question> {
    return try {
        val inputStream = context.resources.openRawResource(R.raw.quiz_fr)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val json = reader.readText()

        Json.decodeFromString<List<Question>>(json)
    } catch (e: IOException) {
        emptyList()
    } catch (e: Resources.NotFoundException) {
        emptyList()
    }
}
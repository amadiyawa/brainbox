package com.amadiyawa.feature_quiz.data.util

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.amadiyawa.feature_quiz.R
import com.amadiyawa.feature_quiz.domain.model.Question
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun loadQuestionList(context: Context): List<Question> {
    Log.d("QuestionDatabase", "Creating QuestionDatabase")
    return try {
        val inputStream = context.resources.openRawResource(R.raw.quiz_fr)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val json = reader.readText()
        Log.d("loadQuestionList", json)

        Json.decodeFromString<List<Question>>(json)
    } catch (e: IOException) {
        Log.e("loadQuestionList", "Error reading raw resource", e)
        emptyList()
    } catch (e: Resources.NotFoundException) {
        Log.e("loadQuestionList", "Raw resource not found", e)
        emptyList()
    }
}
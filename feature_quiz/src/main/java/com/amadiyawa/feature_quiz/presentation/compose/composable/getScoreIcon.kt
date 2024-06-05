package com.amadiyawa.feature_quiz.presentation.compose.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.amadiyawa.feature_base.presentation.compose.composable.DialogIcon

@Composable
fun getScoreIcon(score: Int) : DialogIcon {
    return if (score < 10) {
        DialogIcon(
            icon = Icons.Filled.MoodBad,
            tint = MaterialTheme.colorScheme.error
        )
    } else if (score < 20) {
        DialogIcon(
            icon = Icons.Filled.SentimentSatisfied,
            tint = MaterialTheme.colorScheme.secondary
        )
    } else {
        DialogIcon(
            icon = Icons.Filled.Mood,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
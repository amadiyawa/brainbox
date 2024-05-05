package com.amadiyawa.feature_quiz.presentation.screen.scorelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.common.util.isScrollingUp
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_quiz.presentation.compose.composable.FloatingActionButton
import com.amadiyawa.feature_quiz.presentation.compose.composable.Toolbar

@Composable
fun ScoreListScreen(
    onQuestionListClick: () -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        floatingActionButton = {
            FloatingActionButton(
                imageVector = Icons.Filled.Add,
                onClick = onQuestionListClick,
                isVisible = listState.isScrollingUp()
            )
        },
        topBar = { SetUpToolbar() },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        SetupContent(
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun SetUpToolbar() {
    Toolbar(centered = false, title = "BrainBox", null)
}

@Composable
private fun SetupContent(
    paddingValues: PaddingValues,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ){
        HandleUiState()
    }
}

@Composable
private fun HandleUiState() {
    UserCard()
}

@Composable
private fun UserCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier.padding(Dimen.Spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextTitleLarge(
                text = "Coming soon..",
            )
        }
    }
}
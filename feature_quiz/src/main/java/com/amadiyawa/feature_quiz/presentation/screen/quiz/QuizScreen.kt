package com.amadiyawa.feature_quiz.presentation.screen.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.compose.composable.DataNotFoundAnim
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_quiz.R
import com.amadiyawa.feature_quiz.presentation.compose.composable.Toolbar
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuizScreen(
    onBackClick: () -> Unit
) {
    val viewModel: QuizViewModel = koinViewModel()
    viewModel.onEnter()

    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = { SetUpToolbar(onBackClick) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        SetupContent(
            paddingValues = paddingValues,
            viewModel = viewModel
        )
    }
}

@Composable
private fun SetUpToolbar(onBackClick: () -> Unit) {
    Toolbar(
        centered = true,
        title = stringResource(id = R.string.new_game),
        onBackClick = onBackClick
    )
}

@Composable
private fun SetupContent(
    paddingValues: PaddingValues,
    viewModel: QuizViewModel
) {
    val uiState: QuizViewModel.UiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){
        HandleUiState(uiState = uiState)
    }
}

@Composable
private fun HandleUiState(
    uiState: QuizViewModel.UiState,
) {
    uiState.let {
        when (it) {
            QuizViewModel.UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingAnimation(visible = true)
                }
            }
            QuizViewModel.UiState.Error -> {
                DataNotFoundAnim()
            }
            QuizViewModel.UiState.Content -> {
                val quiz = it.
            }
        }
    }
}

@Composable
private fun Question() {
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
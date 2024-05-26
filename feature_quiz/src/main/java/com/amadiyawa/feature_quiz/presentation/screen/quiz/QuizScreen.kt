package com.amadiyawa.feature_quiz.presentation.screen.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.compose.composable.DataNotFoundAnim
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleMedium
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
    viewModel: QuizViewModel,
) {
    val uiState: QuizViewModel.UiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){
        HandleUiState(
            viewModel = viewModel,
            uiState = uiState
        )
    }
}

@Composable
private fun HandleUiState(
    viewModel: QuizViewModel,
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
            is QuizViewModel.UiState.Content -> {
                if (it.playerName.isNotBlank()) {
                    Quiz(
                        content = it,
                        viewModel = viewModel
                    )
                } else {
                    GetPlayerName(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun Quiz(
    content: QuizViewModel.UiState.Content,
    viewModel: QuizViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.Padding.screenContent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimen.Padding.screenContent)
    ) {
        LevelIndicator(content = content)
        QuizQuestion(
            content = content,
            viewModel = viewModel
        )
        NextQuizQuestion(viewModel = viewModel)
    }
}

@Composable
private fun LevelIndicator(content: QuizViewModel.UiState.Content) {
    val currentLevel = content.currentQuestion.level

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LevelLine(isActive = currentLevel >= 1)
        LevelLine(isActive = currentLevel >= 2)
        LevelLine(isActive = currentLevel >= 3)
    }
}

@Composable
private fun RowScope.LevelLine(isActive: Boolean) {
    Row(
        modifier = Modifier
            .height(4.dp)
            .weight(1f)
            .background(if (isActive) MaterialTheme.colorScheme.primary else Color.Gray)
    ){}
}

@Composable
private fun QuizQuestion(
    content: QuizViewModel.UiState.Content,
    viewModel: QuizViewModel
) {
    val question = content.currentQuestion
    val currentSelectedOption by viewModel.currentSelectedOption.collectAsStateWithLifecycle()

    // Launch a coroutine when the QuizQuestion composable is called
    LaunchedEffect(key1 = question) {
        // Start the timer
        viewModel.startTimer()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimen.Padding.screenContent)
    ){
        TextTitleLarge(
            modifier = Modifier
                .fillMaxWidth(),
            text = question.question
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.spacedBy(Dimen.Spacing.medium)        ) {
            question.optionList.forEach { option ->
                Card(
                    onClick = { viewModel.onOptionSelected(option = option) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentSelectedOption == option) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    TextTitleMedium(
                        modifier = Modifier
                            .padding(Dimen.Padding.screenContent),
                        text = option
                    )
                }
            }
        }
    }
}

@Composable
private fun NextQuizQuestion(
    viewModel: QuizViewModel
) {
    val currentSelectedOption by viewModel.currentSelectedOption.collectAsStateWithLifecycle()
    val remainingTime by viewModel.remainingTime.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = { viewModel.nextQuestion() },
                enabled = currentSelectedOption.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape
            ) {
                Row(
                    modifier = Modifier.padding(Dimen.Padding.screenContent),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        val minutes = remainingTime / 60
                        val seconds = remainingTime % 60
                        Text(text = String.format("%02d:%02d", minutes, seconds))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Text(text = stringResource(id = R.string.next_question))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(id = R.string.next_question)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GetPlayerName(
    viewModel: QuizViewModel
) {
    val playerName by viewModel.playerName.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(Dimen.Padding.screenContent)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimen.Padding.screenContent)
        ) {
            TextTitleLarge(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.player_name_input)
            )

            OutlinedTextField(
                value = playerName,
                onValueChange = { viewModel.onPlayerNameChanged(it) },
                label = { Text(text = stringResource(id = R.string.player_name)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = Dimen.Size.extraLarge),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            Button(
                onClick = { viewModel.setPlayerName() },
                enabled = playerName.isNotBlank() && playerName.length > 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimen.Size.extraLarge)
            ) {
                Text(text = stringResource(id = R.string.game))
            }
        }
    }
}
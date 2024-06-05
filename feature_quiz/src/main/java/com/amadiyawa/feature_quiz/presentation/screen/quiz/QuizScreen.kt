package com.amadiyawa.feature_quiz.presentation.screen.quiz

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.compose.composable.DataNotFoundAnim
import com.amadiyawa.feature_base.presentation.compose.composable.DialogInfo
import com.amadiyawa.feature_base.presentation.compose.composable.DialogInfoText
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleMedium
import com.amadiyawa.feature_quiz.R
import com.amadiyawa.feature_quiz.presentation.compose.composable.Toolbar
import com.amadiyawa.feature_quiz.presentation.compose.composable.getScoreIcon
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

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
    val uiState: QuizViewModel.UiState = viewModel.uiStateFlow.collectAsStateWithLifecycle().value

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
            is QuizViewModel.UiState.Quiz -> {
                Quiz(
                    quiz = it,
                    viewModel = viewModel
                )
            }
            is QuizViewModel.UiState.PlayerName -> {
                GetPlayerName(viewModel = viewModel)
            }
            is QuizViewModel.UiState.GameOver -> {
                val showScore = remember { mutableStateOf(true) }

                AnimatedVisibility(visible = showScore.value) {
                    DialogInfo(
                        dialogInfoText = DialogInfoText(
                            title = stringResource(id = R.string.game_over),
                            message = stringResource(
                                id = R.string.game_over_message,
                                it.playerName,
                                it.points,
                                it.totalPoints
                            ),
                            confirmText = stringResource(id = R.string.understood)
                        ),
                        dialogIcon = getScoreIcon(score = it.points),
                        onDismiss = {
                            showScore.value = false
                            viewModel.saveOrUpdatePlayer(
                                playerName = it.playerName,
                                points = it.points
                            )
                        },
                        onConfirm = {
                            showScore.value = false
                            viewModel.saveOrUpdatePlayer(
                                playerName = it.playerName,
                                points = it.points
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Quiz(
    quiz: QuizViewModel.UiState.Quiz,
    viewModel: QuizViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.Padding.screenContent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimen.Padding.screenContent)
    ) {
        LevelIndicator(quiz = quiz)
        QuizQuestion(
            quiz = quiz,
            viewModel = viewModel
        )
        NextQuizQuestion(viewModel = viewModel)
    }
}

@Composable
private fun LevelIndicator(quiz: QuizViewModel.UiState.Quiz) {
    val currentLevel = quiz.currentQuestion.level

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LevelLine(isActive = currentLevel >= 1, level = 1)
        LevelLine(isActive = currentLevel >= 2, level = 2)
        LevelLine(isActive = currentLevel >= 3, level = 3)
    }
}

@Composable
private fun RowScope.LevelLine(
    isActive: Boolean,
    level: Int
) {
    when (level) {
        1 -> {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp), // Increase the height to accommodate the circle and rectangle
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle with text at its center
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(50.dp) // Set the width of the circle
                        .height(50.dp) // Set the height of the circle
                        .background(
                            color = getActiveColor(isActive = isActive),
                            shape = CircleShape
                        )
                ) {
                    TextTitleMedium(text = "1") // Replace "1" with the actual text you want to display
                }

                // Rectangle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp) // Set the height of the rectangle
                        .offset(x = (-10).dp) // Offset the rectangle to the left
                        .background(
                            color = getActiveColor(isActive = isActive),
                            shape = RectangleShape
                        )
                )
            }
        }
        2 -> {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp), // Increase the height to accommodate the circle and rectangle
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(20.dp) // Set the height of the rectangle
                        .background(
                            color = getActiveColor(isActive = isActive),
                            shape = RectangleShape
                        )
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(50.dp) // Set the width of the circle
                        .height(50.dp) // Set the height of the circle
                        .background(
                            color = getActiveColor(isActive = isActive),
                            shape = CircleShape
                        )
                ) {
                    TextTitleMedium(text = "2") // Replace "1" with the actual text you want to display
                }

                // Rectangle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp) // Set the height of the rectangle
                        .offset(x = (-10).dp) // Offset the rectangle to the left
                        .background(
                            color = getActiveColor(isActive = isActive),
                            shape = RectangleShape
                        )
                )
            }
        }
        3 -> {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp), // Increase the height to accommodate the circle and rectangle
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(20.dp) // Set the height of the rectangle
                        .offset(x = 10.dp) // Offset the rectangle to the left
                        .background(
                            color = getActiveColor(isActive = isActive),
                            shape = RectangleShape
                        )
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(50.dp) // Set the width of the circle
                        .height(50.dp) // Set the height of the circle
                        .background(
                            color = getActiveColor(isActive = isActive),
                            shape = CircleShape
                        )
                ) {
                    TextTitleMedium(text = "3")
                }
            }
        }
    }
}

@Composable
private fun QuizQuestion(
    quiz: QuizViewModel.UiState.Quiz,
    viewModel: QuizViewModel
) {
    val question = quiz.currentQuestion
    val currentSelectedOption = viewModel.currentSelectedOption.collectAsStateWithLifecycle()

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
                        containerColor = if (currentSelectedOption.value == option) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    TextTitleMedium(
                        modifier = Modifier.padding(Dimen.Padding.screenContent),
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
    val currentSelectedOption = viewModel.currentSelectedOption.collectAsStateWithLifecycle()
    val remainingTime = viewModel.remainingTime.collectAsStateWithLifecycle()

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
                enabled = currentSelectedOption.value.isNotBlank(),
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
                        val minutes = remainingTime.value / 60
                        val seconds = remainingTime.value % 60
                        Text(
                            text = String.format(
                                Locale.getDefault(), "%02d:%02d", minutes, seconds
                            )
                        )
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
    val playerName = viewModel.playerName.collectAsStateWithLifecycle()

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
                value = playerName.value,
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
                enabled = playerName.value.isNotBlank() && playerName.value.length > 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimen.Size.extraLarge)
            ) {
                Text(text = stringResource(id = R.string.game))
            }
        }
    }
}

@Composable
fun getActiveColor(isActive: Boolean): Color {
    return if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
}
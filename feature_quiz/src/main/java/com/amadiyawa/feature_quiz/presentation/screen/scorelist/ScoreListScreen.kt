package com.amadiyawa.feature_quiz.presentation.screen.scorelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.common.util.formatDate
import com.amadiyawa.feature_base.common.util.formatTime
import com.amadiyawa.feature_base.common.util.isScrollingUp
import com.amadiyawa.feature_base.presentation.compose.composable.ExpandableRow
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleMedium
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleSmall
import com.amadiyawa.feature_quiz.R
import com.amadiyawa.feature_quiz.domain.model.Player
import com.amadiyawa.feature_quiz.domain.model.Score
import com.amadiyawa.feature_quiz.presentation.compose.composable.FloatingActionButton
import com.amadiyawa.feature_quiz.presentation.compose.composable.Toolbar
import com.amadiyawa.feature_quiz.presentation.compose.composable.getScoreIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScoreListScreen(
    onQuestionListClick: () -> Unit
) {
    val viewModel: ScoreListViewModel = koinViewModel()
    viewModel.onEnter()
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
            viewModel = viewModel,
            listState = listState
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
    viewModel: ScoreListViewModel,
    listState: LazyListState
) {
    val uiState: ScoreListViewModel.UiState = viewModel.uiStateFlow.collectAsStateWithLifecycle().value

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ){
        HandleUiState(
            uiState = uiState,
            listState = listState
        )
    }
}

@Composable
private fun HandleUiState(
    uiState: ScoreListViewModel.UiState,
    listState: LazyListState
) {
    when (uiState) {
        is ScoreListViewModel.UiState.Loading -> {
            // Show loading
        }
        is ScoreListViewModel.UiState.Error -> {
            // Show error
        }
        is ScoreListViewModel.UiState.Empty -> {
            // Show empty
        }
        is ScoreListViewModel.UiState.PlayerList -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                content = {
                    items(uiState.playerList.size) { index ->
                        UserCard(
                            player = uiState.playerList[index],
                        )
                    }
                },
                state = listState,
            )
        }
    }
}

@Composable
private fun UserCard(player: Player) {
    val expandedScores = remember { mutableStateOf (false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.Spacing.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextTitleLarge(
                text = player.fullName,
            )
        }

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.background)

        ExpandableRow(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            expanded = expandedScores.value,
            fixedLabel = stringResource(id = R.string.scores),
            onRowClick = {
                expandedScores.value = !expandedScores.value
            }
        )

        AnimatedVisibility(visible = expandedScores.value) {
            PlayerScores(scoreList = player.scoreList)
        }
    }
}

@Composable
private fun PlayerScores(
    scoreList: List<Score>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimen.Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Dimen.Spacing.medium)
    ) {
        scoreList.forEach {
            val icon = getScoreIcon(score = it.point)
            val time = formatDate(it.createdDate) + " " + stringResource(id = R.string.at) + " " +
                    formatTime(it.createdDate)
            Row {
                TextTitleMedium(text = it.point.toString())

                Spacer(Modifier.weight(1f))

                TextTitleSmall(text = time)

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = icon.icon,
                    contentDescription = it.point.toString(),
                    tint = icon.tint
                )
            }
        }
    }
}
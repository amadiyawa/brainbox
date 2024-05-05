package com.amadiyawa.brainbox.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination
import com.amadiyawa.feature_quiz.presentation.navigation.QuestionListNavigation
import com.amadiyawa.feature_quiz.presentation.navigation.ScoreListNavigation
import com.amadiyawa.feature_quiz.presentation.navigation.questionListGraph
import com.amadiyawa.feature_quiz.presentation.navigation.scoreListGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    onNavigateToDestination: (AppNavigationDestination, String?) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = ScoreListNavigation.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        scoreListGraph(
            onNavigateToQuestionList = {
                onNavigateToDestination(QuestionListNavigation, QuestionListNavigation.route)
            },
            nestedGraph = {
                questionListGraph(
                    onBackClick = onBackClick
                )
            }
        )
    }
}
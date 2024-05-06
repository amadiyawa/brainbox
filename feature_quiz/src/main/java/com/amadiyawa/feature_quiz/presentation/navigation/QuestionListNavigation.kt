package com.amadiyawa.feature_quiz.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination
import com.amadiyawa.feature_quiz.presentation.screen.quiz.QuizScreen

/**
 * Defines the navigation route and destination for the score list feature.
 */
object QuestionListNavigation : AppNavigationDestination {
    private const val QUESTION_LIST = "question_list"

    // The route for the score list feature
    override val route: String = QUESTION_LIST

    // The destination name for the score list feature
    override val destination: String = "question_list_destination"
}

fun NavGraphBuilder.questionListGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = QuestionListNavigation.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        }
    ) {
        QuizScreen(onBackClick = onBackClick)
    }
}
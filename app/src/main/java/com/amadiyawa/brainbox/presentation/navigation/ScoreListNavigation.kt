package com.amadiyawa.brainbox.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.amadiyawa.brainbox.presentation.screen.scorelist.ScoreListScreen

/**
 * Defines the navigation route and destination for the score list feature.
 */
object ScoreListNavigation : AppNavigationDestination {
    private const val SCORE_LIST = "score_list"

    // The route for the score list feature
    override val route: String = SCORE_LIST

    // The destination name for the score list feature
    override val destination: String = "score_list_destination"
}

fun NavGraphBuilder.scoreListGraph(
    onNavigateToQuestionList: () -> Unit,
    nestedGraph: NavGraphBuilder.() -> Unit
) {
    navigation(startDestination = ScoreListNavigation.destination, route = ScoreListNavigation.route) {
        composable(
            route = ScoreListNavigation.destination,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -1000 },
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -1000 },
                    animationSpec = tween(500)
                )
            }
        ) {
            ScoreListScreen(onQuestionListClick = onNavigateToQuestionList)
        }
        nestedGraph()
    }
}
package com.amadiyawa.feature_quiz.presentation.compose.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleSmall
import com.amadiyawa.feature_quiz.R

@Composable
fun FloatingActionButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    isVisible: Boolean,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically {
            with(density) { 40.dp.roundToPx() }
        } + fadeIn(),
        exit = fadeOut(
            animationSpec = keyframes {
                this.durationMillis = 120
            }
        )
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector,"", tint = MaterialTheme.colorScheme.onPrimary)
                TextTitleSmall(text = stringResource(id = R.string.new_game))
            }
        }
    }
}
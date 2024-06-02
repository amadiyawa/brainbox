package com.amadiyawa.feature_base.common.res

import androidx.compose.ui.unit.dp

/**
 * Contains dimension values that are used across the application.
 */
object Dimen {
    // Spacing dimensions
    object Spacing {
        val small = 4.dp
        val medium = 8.dp
        val large = 16.dp
        val extraLarge = 32.dp
        val extraExtraLarge = 64.dp
    }

    object DialogCardSize {
        val minWidthPercent = 0.6f  // 60% of screen width
        val minHeightPercent = 0.4f  // 40% of screen height
        val maxWidthPercent = 0.8f  // 80% of screen width
        val maxHeightPercent = 0.6f  // 60% of screen height
    }

    object Size {
        val extraSmall = 16.dp
        val small = 24.dp
        val medium = 32.dp
        val large = 40.dp
        val extraLarge = 56.dp
    }

    // Padding dimensions
    object Padding {
        val screenContent = Spacing.large
    }

    // Image dimensions
    object Image {
        val size = 100.dp
    }

    // Picture dimensions
    object Picture {
        val smallSize = 48.dp
        val mediumSize = 72.dp
    }
}

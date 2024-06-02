package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lens
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.amadiyawa.feature_base.common.res.Dimen

data class DialogInfoText(
    val title: String,
    val message: String,
    val confirmText: String
)

data class DialogIcon(
    val icon: ImageVector,
    val tint: Color,
)

@Composable
fun DialogInfo(
    dialogInfoText: DialogInfoText,
    dialogIcon: DialogIcon,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimen.Spacing.large),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimen.Spacing.medium)
                ) {
                    Icon(
                        imageVector = dialogIcon.icon,
                        contentDescription = dialogInfoText.title,
                        tint = dialogIcon.tint,
                        modifier = Modifier.size(Dimen.Size.extraLarge)
                    )

                    TextTitleLarge(text = dialogInfoText.title)

                    TextTitleMedium(text = dialogInfoText.message)

                    Button(
                        onClick = { onConfirm() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row {
                            Text(text = dialogInfoText.confirmText)
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun DialogInfoPreview() {
    DialogInfo(
        dialogInfoText = DialogInfoText(
            title = "Title",
            message = "Message",
            confirmText = "Confirm"
        ),
        dialogIcon = DialogIcon(
            icon = Icons.Filled.Lens,
            tint = MaterialTheme.colorScheme.primary
        ),
        onDismiss = {},
        onConfirm = {}
    )
}
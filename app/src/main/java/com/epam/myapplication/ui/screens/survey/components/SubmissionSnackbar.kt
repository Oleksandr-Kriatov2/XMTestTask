package com.epam.myapplication.ui.screens.survey.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epam.myapplication.R
import com.epam.myapplication.ui.screens.survey.SubmissionResult

@Composable
fun SubmissionSnackbarHost(
    snackbarHostState: SnackbarHostState,
    onRetry: () -> Unit
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = {
            SubmissionSnackbar(visuals = it.visuals as SubmissionResultSnackbarVisuals) { onRetry() }
        }
    )
}

@Composable
fun SubmissionSnackbar(
    visuals: SubmissionResultSnackbarVisuals,
    onRetry: () -> Unit
) {
    val submissionResult = visuals.result
    val text =
        if (submissionResult is SubmissionResult.Success) stringResource(id = R.string.success) else stringResource(
            id = R.string.failure
        )
    val backgroundColor =
        if (submissionResult is SubmissionResult.Success) Color.Green else Color.Red
    Snackbar(
        shape = RectangleShape,
        containerColor = backgroundColor,
        modifier = Modifier
            .padding(0.dp)
            .height(120.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (submissionResult is SubmissionResult.Success) Arrangement.Start else Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            if (submissionResult is SubmissionResult.Failure) {
                OutlinedButton(onClick = onRetry) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.retry)
                    )
                }
            }
        }
    }
}

class SubmissionResultSnackbarVisuals(
    val result: SubmissionResult,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val message: String = "",
    override val withDismissAction: Boolean = false
) : SnackbarVisuals


@Composable
@Preview(showBackground = true)
fun PreviewSubmissionSnackbarSuccess() {
    SubmissionSnackbar(
        visuals = SubmissionResultSnackbarVisuals(SubmissionResult.Success),
        onRetry = { /* Retry logic */ }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewSubmissionSnackbarFailure() {
    SubmissionSnackbar(
        visuals = SubmissionResultSnackbarVisuals(SubmissionResult.Failure),
        onRetry = { /* Retry logic */ }
    )
}
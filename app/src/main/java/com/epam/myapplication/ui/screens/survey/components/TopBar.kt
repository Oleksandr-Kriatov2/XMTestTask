package com.epam.myapplication.ui.screens.survey.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epam.myapplication.R

@Composable
fun TopBar(
    currentQuestionIndex: Int,
    totalQuestions: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    isPreviousEnabled: Boolean,
    isNextEnabled: Boolean,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
        }
        Text(
            text = stringResource(
                id = R.string.question,
                if (totalQuestions == 0) 0 else currentQuestionIndex + 1,
                totalQuestions
            ),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Row {
            TextButton(onClick = onPrevious, enabled = isPreviousEnabled) {
                Text(stringResource(id = R.string.previous), fontSize = 12.sp)
            }
            TextButton(onClick = onNext, enabled = isNextEnabled) {
                Text(stringResource(id = R.string.next), fontSize = 12.sp)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewTopBarFirstQuestion() {
    TopBar(
        currentQuestionIndex = 0,
        totalQuestions = 10,
        onPrevious = {},
        onNext = {},
        isPreviousEnabled = false,
        isNextEnabled = true,
        onBack = {}
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewTopBarMiddleQuestion() {
    TopBar(
        currentQuestionIndex = 5,
        totalQuestions = 10,
        onPrevious = {},
        onNext = {},
        isPreviousEnabled = true,
        isNextEnabled = true,
        onBack = {}
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewTopBarLastQuestion() {
    TopBar(
        currentQuestionIndex = 9,
        totalQuestions = 10,
        onPrevious = {},
        onNext = {},
        isPreviousEnabled = true,
        isNextEnabled = false,
        onBack = {}
    )
}
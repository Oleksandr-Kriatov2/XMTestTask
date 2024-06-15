package com.epam.myapplication.ui.screens.survey.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epam.myapplication.R
import com.epam.myapplication.ui.screens.survey.SurveyQuestion

@Composable
fun QuestionContent(
    question: SurveyQuestion,
    onAnswerChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isSubmitting: Boolean
) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = question.text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        BasicTextField(
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            singleLine = true,
            enabled = question.isSubmitted.not() && isSubmitting.not(),
            value = question.answer.orEmpty(),
            onValueChange = onAnswerChange,
            decorationBox = { innerTextField ->
                if (question.answer.isNullOrEmpty()) {
                    Text(text = stringResource(id = R.string.answer_hint))
                } else innerTextField()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray.copy(alpha = 0.5f))
                .padding(16.dp)
        )
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewQuestionContent() {
    QuestionContent(
        question = SurveyQuestion(
            id = 1,
            text = "What is your favorite food?",
            answer = null,
            isSubmitted = false
        ),
        onAnswerChange = {},
        onSubmit = {},
        isSubmitting = false
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewQuestionContentSubmitted() {
    QuestionContent(
        question = SurveyQuestion(
            id = 1,
            text = "What is your favorite food?",
            answer = "Pizza",
            isSubmitted = true
        ),
        onAnswerChange = {},
        onSubmit = {},
        isSubmitting = false
    )
}
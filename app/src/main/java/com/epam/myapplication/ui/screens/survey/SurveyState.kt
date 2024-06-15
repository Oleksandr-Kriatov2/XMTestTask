package com.epam.myapplication.ui.screens.survey

import com.epam.myapplication.common.UiDataEvent

data class SurveyState(
    val questions: List<SurveyQuestion>,
    val currentQuestionIndex: Int = 0,
    val submittedAnswersCount: Int = 0,
    val isLoading: Boolean = false,
    val submissionResult: UiDataEvent<SubmissionResult>? = null,
)
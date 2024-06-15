package com.epam.myapplication.ui.screens.survey

sealed class SubmissionResult {
    data object Success : SubmissionResult()
    data object Failure : SubmissionResult()
}
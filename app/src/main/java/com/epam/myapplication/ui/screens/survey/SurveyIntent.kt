package com.epam.myapplication.ui.screens.survey

sealed class SurveyIntent {
    data object LoadQuestions : SurveyIntent()
    data object PreviousQuestion : SurveyIntent()
    data object NextQuestion : SurveyIntent()
    data class SubmitAnswer(val questionId: Int) : SurveyIntent()
    data class UpdateAnswer(val questionId: Int, val newAnswer: String) : SurveyIntent()
    data class OnNewQuestionVisible(val position: Int) : SurveyIntent()
}
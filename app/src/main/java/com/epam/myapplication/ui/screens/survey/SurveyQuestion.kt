package com.epam.myapplication.ui.screens.survey

data class SurveyQuestion(
    val id: Int,
    val text: String,
    val answer: String? = null,
    val isSubmitted: Boolean = false
)
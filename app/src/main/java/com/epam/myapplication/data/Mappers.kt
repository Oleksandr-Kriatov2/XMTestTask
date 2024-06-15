package com.epam.myapplication.data

import com.epam.myapplication.data.model.QuestionApiModel
import com.epam.myapplication.ui.screens.survey.SurveyQuestion

fun QuestionApiModel.toUI(): SurveyQuestion {
    return SurveyQuestion(id = id, text = question)
}
package com.epam.myapplication.data.repository

import com.epam.myapplication.common.Result
import com.epam.myapplication.ui.screens.survey.SurveyQuestion

interface Repository {
    suspend fun getQuestions(): Result<List<SurveyQuestion>>

    suspend fun submitAnswer(questionId: Int, answer: String): Result<Unit>
}
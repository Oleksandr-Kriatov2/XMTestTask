package com.epam.myapplication.data.repository

import com.epam.myapplication.data.model.AnswerApiModel
import com.epam.myapplication.data.ApiService
import com.epam.myapplication.common.Result
import com.epam.myapplication.data.toUI
import com.epam.myapplication.ui.screens.survey.SurveyQuestion
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val api: ApiService): Repository {

    override suspend fun getQuestions(): Result<List<SurveyQuestion>> {
        return try {
            val response = api.getQuestions()
            Result.Success(response.map { it.toUI() })
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun submitAnswer(questionId: Int, answer: String): Result<Unit> {
        return try {
            api.submitAnswer(AnswerApiModel(questionId, answer))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
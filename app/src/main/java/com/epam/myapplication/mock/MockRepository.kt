package com.epam.myapplication.mock

import com.epam.myapplication.common.Result
import com.epam.myapplication.data.repository.Repository
import com.epam.myapplication.ui.screens.survey.SurveyQuestion

class MockRepository : Repository {
    override suspend fun getQuestions(): Result<List<SurveyQuestion>> {
        TODO("Not yet implemented")
    }

    override suspend fun submitAnswer(
        questionId: Int,
        answer: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }


}
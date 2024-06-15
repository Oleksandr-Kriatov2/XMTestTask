package com.epam.myapplication.data

import com.epam.myapplication.data.model.AnswerApiModel
import com.epam.myapplication.data.model.QuestionApiModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("questions")
    suspend fun getQuestions(): List<QuestionApiModel>

    @POST("question/submit")
    suspend fun submitAnswer(@Body answer: AnswerApiModel)
}
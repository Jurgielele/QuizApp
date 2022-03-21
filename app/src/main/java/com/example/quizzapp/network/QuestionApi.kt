package com.example.quizzapp.network

import com.example.quizzapp.data.Question
import retrofit2.http.GET


interface QuestionApi {
    @GET("world.json")
    suspend fun getQuestions(): Question
}
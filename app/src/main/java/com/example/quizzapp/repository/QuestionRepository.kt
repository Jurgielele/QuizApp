package com.example.quizzapp.repository

import com.example.quizzapp.data.DataOrException
import com.example.quizzapp.data.Question
import com.example.quizzapp.network.QuestionApi
import java.lang.Exception
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    private val dataOrException = DataOrException<Question, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<Question, Boolean, Exception>{
        try {
            dataOrException.loading = true
            dataOrException.data = api.getQuestions()
            if(dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        }catch (e: Exception){
            dataOrException.e = e
        }
        return dataOrException
    }
}
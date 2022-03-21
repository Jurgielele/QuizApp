package com.example.quizzapp.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizzapp.data.DataOrException
import com.example.quizzapp.data.Question
import com.example.quizzapp.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val repository: QuestionRepository): ViewModel() {
    val data: MutableState<DataOrException<Question, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, null, null))

    init {
        getQuestions()
    }

     private fun getQuestions(){
         viewModelScope.launch {
            data.value = repository.getAllQuestions()
         }
    }
}
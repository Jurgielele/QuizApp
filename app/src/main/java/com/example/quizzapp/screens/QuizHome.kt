package com.example.quizzapp.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizzapp.component.Questions


@Composable
fun QuizHome(viewModel: QuestionsViewModel = hiltViewModel()){
    Questions(viewModel = viewModel)
}

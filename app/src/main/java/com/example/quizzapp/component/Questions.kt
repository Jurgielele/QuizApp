package com.example.quizzapp.component

import android.view.Surface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizzapp.data.QuestionItem
import com.example.quizzapp.screens.QuestionsViewModel


@Composable
fun Questions(viewModel: QuestionsViewModel){
    val questions = viewModel.data.value.data

    val questionIndex = rememberSaveable(){
        mutableStateOf(0)
    }


    if(viewModel.data.value.loading == true){
        CircularProgressIndicator()
    }else{
        if (questions != null) {
            QuestionDisplay(question = questions[questionIndex.value],
                questionIndex = questionIndex.value,
                totalQuestions = questions.size,
                onNextClicked = {
                questionIndex.value = questionIndex.value + 1
            } )
        }
    }
}

@Composable
fun QuestionDisplay(question: QuestionItem,questionIndex: Int,totalQuestions: Int, onNextClicked: (Int) -> Unit){

    val choicesState = remember(question){
        question.choices.toMutableList()
    }
    val answerState = remember(question){
        mutableStateOf<Int?>(null)
    }
    val correctAnswer = remember(question){
        mutableStateOf<Boolean?>(null)
    }


    val score = remember(){ mutableStateOf(0)}
    val updateAnswer: (Int) -> Unit = remember(question){{
        answerState.value = it
        correctAnswer.value = choicesState[it] == question.answer

        if(correctAnswer.value!!){
            score.value = score.value + 3
        }else{
            score.value = score.value - 2
        }

    }}

    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        color = Color(0xff000051)) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuestionCounter(questionIndex, totalQuestions)
                ShowScore(score = score.value)
            }
            DrawDottedLine()

            Column() {
                QuestionCard(question)
                choicesState.forEachIndexed { index, it ->
                    AnswerRow(answerState, index, updateAnswer, correctAnswer, it)
                }
            }
            if(correctAnswer.value==true){
                Button(
                    enabled = correctAnswer.value == true,
                    onClick = { onNextClicked(questionIndex) },
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(10.dp),
                ) {
                    Text(text = "Next", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

        }
    }
}

@Composable
private fun QuestionCard(question: QuestionItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(200.dp, 350.dp)
            .padding(10.dp),
        backgroundColor = Color.Transparent, elevation = 0.dp,
    ) {
        Text(
            text = question.question,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White
        )
    }
}

@Composable
private fun AnswerRow(
    answerState: MutableState<Int?>,
    index: Int,
    updateAnswer: (Int) -> Unit,
    correctAnswer: MutableState<Boolean?>,
    it: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(12.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(20))
            .clip(RoundedCornerShape(20)),
        verticalAlignment = CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.padding(4.dp),
            selected = answerState.value == index,
            onClick = { updateAnswer(index) },
            colors = RadioButtonDefaults.colors(
                selectedColor =
                if (correctAnswer.value == true) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        )
        val text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                    if (correctAnswer.value == true && answerState.value == index) {
                        Color.Green
                    } else if (correctAnswer.value == false && answerState.value == index) {
                        Color.Red
                    } else {
                        Color.White
                    }
                )
            ) {
                append(it)
            }
        }
        Text(text)
    }
}

@Composable
fun ShowScore(score: Int){
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White, fontSize = 27.sp)){
            append("Score: ")
        }
        withStyle(style = SpanStyle(color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)){
            append(score.toString())
        }
    }
    Text(text)
}

@Composable
fun QuestionCounter(currentQuestionIndex: Int, totalQuestions: Int){
    Row(modifier = Modifier
        .padding(16.dp)
        .background(Color.Transparent)) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White, fontSize = 27.sp)){
                    append(currentQuestionIndex.toString())
                }
                withStyle(style = SpanStyle(color = Color.White, fontSize = 17.sp,),){
                    append("/${totalQuestions}")
                }
            }
        )
    }
}

@Composable
fun DrawDottedLine(){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp) , ){
        drawLine(
            color = Color.White,
            start = Offset(0f,0f),
            end = Offset(size.width,0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f), 0f)
        )
    }
}



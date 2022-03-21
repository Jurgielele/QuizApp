package com.example.quizzapp.di

import com.example.quizzapp.network.QuestionApi
import com.example.quizzapp.repository.QuestionRepository
import com.example.quizzapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //provide repository
    @Singleton
    @Provides
    fun provideRepository(api: QuestionApi): QuestionRepository = QuestionRepository(api)

    //provide retrofit
    @Singleton
    @Provides
    fun provideQuestionApi(): QuestionApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionApi::class.java)
    }
}


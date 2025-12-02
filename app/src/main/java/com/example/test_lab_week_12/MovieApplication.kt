package com.example.test_lab_week_13

import android.app.Application
import com.example.test_lab_week_13.api.MovieService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
class MovieApplication : Application() {
    lateinit var movieRepository: MovieRepository
    override fun onCreate() {
        super.onCreate()
// create a Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
// create a MovieService instance
// and bind the MovieService interface to the Retrofit instance
// this allows us to make API calls
        val movieService = retrofit.create(
            MovieService::class.java
        )
// create a MovieRepository instance
        movieRepository = MovieRepository(movieService)
    }
}
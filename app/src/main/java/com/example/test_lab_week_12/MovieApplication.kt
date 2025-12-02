package com.example.test_lab_week_13

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.test_lab_week_12.MovieWorker
import com.example.test_lab_week_12.database.MovieDatabase
import com.example.test_lab_week_13.api.MovieService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

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
        val movieDatabase =
            MovieDatabase.getInstance(applicationContext)
        movieRepository =
            MovieRepository(movieService, movieDatabase)
        val constraints = Constraints.Builder()
            // only run the task if the device is connected to the internet
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
// create a WorkRequest instance
// this will be used to schedule a background task
        val workRequest = PeriodicWorkRequest
            // run the task every 1 hour
            // even if the app is closed or the device is restarted
            .Builder(
                MovieWorker::class.java, 1,
                TimeUnit.HOURS
            ).setConstraints(constraints)
            .addTag("movie-work").build()
// schedule the background task
        WorkManager.getInstance(
            applicationContext
        ).enqueue(workRequest)
    }

}
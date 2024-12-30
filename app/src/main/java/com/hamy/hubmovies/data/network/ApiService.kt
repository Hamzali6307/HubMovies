package com.hamy.hubmovies.data.network

import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("discover/movie?api_key=$API_KEY")
    suspend fun getMovies(): Response<Movies>

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"

    }
}
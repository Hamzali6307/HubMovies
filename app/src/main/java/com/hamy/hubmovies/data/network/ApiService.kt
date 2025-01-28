package com.hamy.hubmovies.data.network

import com.hamy.hubmovies.data.model.MovieDetail
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.model.TrendingMovies
import com.hamy.hubmovies.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie?api_key=$API_KEY")
    suspend fun getMovies(@Query("page") pageNumber:Int): Response<Movies>

    @GET("movie/top_rated?api_key=$API_KEY")
    suspend fun getTopRatedMovies(@Query("page") pageNumber:Int): Response<Movies>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<MovieDetail>

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") time_window: String,
        @Query("api_key") apiKey: String
    ): Response<TrendingMovies>

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"

    }
}
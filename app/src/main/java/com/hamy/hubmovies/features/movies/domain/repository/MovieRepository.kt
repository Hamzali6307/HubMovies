package com.hamy.hubmovies.features.movies.domain.repository

import com.hamy.hubmovies.common.ApiState
import com.hamy.hubmovies.data.model.MovieDetail
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.model.TrendingMovies
import com.hamy.hubmovies.data.model.VideoPlayAbleLink
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(pageNumber: Int): Flow<ApiState<Movies>>
    suspend fun getTopRatedMovies(pageNumber:Int): Flow<ApiState<Movies>>
    suspend fun getMovieDetails(movieId:String): Flow<ApiState<MovieDetail>>
    suspend fun getVideoLink(movieId:Int): Flow<ApiState<VideoPlayAbleLink>>
    suspend fun getTrendingMovies(): Flow<ApiState<TrendingMovies>>
}
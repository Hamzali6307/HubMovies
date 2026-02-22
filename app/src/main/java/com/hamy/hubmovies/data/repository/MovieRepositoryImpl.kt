package com.hamy.hubmovies.data.repository

import com.hamy.hubmovies.common.ApiState
import com.hamy.hubmovies.common.BaseRepository
import com.hamy.hubmovies.data.model.MovieDetail
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.model.TrendingMovies
import com.hamy.hubmovies.data.model.VideoPlayAbleLink
import com.hamy.hubmovies.data.network.ApiService
import com.hamy.hubmovies.features.movies.domain.repository.MovieRepository
import com.hamy.hubmovies.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val apiService: ApiService): MovieRepository,BaseRepository() {
    override suspend fun getMovies(pageNumber: Int): Flow<ApiState<Movies>>  = safeApiCall{
        apiService.getMovies(pageNumber)
    }
    override suspend fun getTopRatedMovies(pageNumber: Int): Flow<ApiState<Movies>>  = safeApiCall{
        apiService.getTopRatedMovies(pageNumber)
    }
    override suspend fun getMovieDetails(movieId:String): Flow<ApiState<MovieDetail>>  = safeApiCall{
        apiService.getMovieDetail(movieId,Constants.API_KEY)
    }
    override suspend fun getVideoLink(movieId:Int): Flow<ApiState<VideoPlayAbleLink>>  = safeApiCall{
        apiService.getVideoPlayAbleLink(movieId)
    }
    override suspend fun getTrendingMovies(): Flow<ApiState<TrendingMovies>>  = safeApiCall{
        apiService.getTrendingMovies("day",Constants.API_KEY)
    }
}
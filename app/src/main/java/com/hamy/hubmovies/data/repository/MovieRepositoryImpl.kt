package com.hamy.hubmovies.data.repository

import com.hamy.hubmovies.common.ApiState
import com.hamy.hubmovies.common.BaseRepository
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.network.ApiService
import com.hamy.hubmovies.features.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val apiService: ApiService): MovieRepository,BaseRepository() {
    override suspend fun getMovies(): Flow<ApiState<Movies>>  = safeApiCall{
        apiService.getMovies()
    }
}
package com.hamy.hubmovies.features.movies.domain.repository

import com.hamy.hubmovies.common.ApiState
import com.hamy.hubmovies.data.model.Movies
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(): Flow<ApiState<Movies>>
}
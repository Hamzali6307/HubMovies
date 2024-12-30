package com.hamy.hubmovies.features.movies.domain.useCase

import com.hamy.hubmovies.common.ApiState
import com.hamy.hubmovies.common.map
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.features.movies.domain.mapper.MovieMapper
import com.hamy.hubmovies.features.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val repo: MovieRepository,
    private val mapper: MovieMapper
) {
    suspend fun getMovies(): kotlinx.coroutines.flow.Flow<ApiState<List<Movies.Results>?>> {
        return repo.getMovies().map { results ->
            results.map {
                mapper.fromMap(it)
            }
        }
    }
}
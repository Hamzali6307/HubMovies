package com.hamy.hubmovies.features.movies.domain.useCase

import com.hamy.hubmovies.common.ApiState
import com.hamy.hubmovies.common.map
import com.hamy.hubmovies.data.model.MovieDetail
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.model.TrendingMovies
import com.hamy.hubmovies.data.model.VideoPlayAbleLink
import com.hamy.hubmovies.features.movies.domain.mapper.MovieMapper
import com.hamy.hubmovies.features.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val repo: MovieRepository,
    private val mapper: MovieMapper
) {
    suspend fun getMovies(pageNumber: Int): Flow<ApiState<List<Movies.Results>?>> {
        return repo.getMovies(pageNumber).map { results ->
            results.map {
                mapper.fromMap(it)
            }
        }
    }
    suspend fun getTopRatedMovies(pageNumber:Int): Flow<ApiState<List<Movies.Results>?>> {
        return repo.getTopRatedMovies(pageNumber).map { results ->
            results.map {
                mapper.fromMap(it)
            }
        }
    }
    suspend fun getMovieDetails(movieId: String): Flow<ApiState<MovieDetail>?> {
        return repo.getMovieDetails(movieId)
    }

    suspend fun getVideoLink(movieId: Int): Flow<ApiState<VideoPlayAbleLink>?> {
        return repo.getVideoLink(movieId)
    }

    suspend fun getTrendingMovies(): Flow<ApiState<TrendingMovies>?> {
        return repo.getTrendingMovies()
    }
}
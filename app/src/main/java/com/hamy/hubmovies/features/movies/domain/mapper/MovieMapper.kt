package com.hamy.hubmovies.features.movies.domain.mapper

import com.hamy.hubmovies.common.Mapper
import com.hamy.hubmovies.data.model.Movies
import javax.inject.Inject

class MovieMapper @Inject constructor(): Mapper<Movies?,List<Movies.Results>?> {
    override fun fromMap(from: Movies?): List<Movies.Results>? {
        return from?.results?.map {
            Movies.Results(
                id = it?.id,
                original_title = it?.original_title,
                overview = it?.overview,
                poster_path = it?.poster_path,
                vote_average = it?.vote_average
            )
        }
    }

}
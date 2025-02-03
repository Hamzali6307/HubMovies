package com.hamy.hubmovies.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamy.hubmovies.common.doOnFailure
import com.hamy.hubmovies.common.doOnLoading
import com.hamy.hubmovies.common.doOnSuccess
import com.hamy.hubmovies.data.model.MovieDetail
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.model.TrendingMovies
import com.hamy.hubmovies.data.model.VideoPlayAbleLink
import com.hamy.hubmovies.features.movies.domain.useCase.MovieUseCase
import com.hamy.hubmovies.utils.Extensions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val useCase: MovieUseCase) : ViewModel() {
    private val _res: MutableState<MovieState> = mutableStateOf(MovieState())
    private val _topRatedMovies: MutableState<MovieState> = mutableStateOf(MovieState())
    private val _movieDetail: MutableState<MovieDetailState> = mutableStateOf(MovieDetailState())
    private val _movieLink : MutableState<VideoLinkState> = mutableStateOf(VideoLinkState())
    private val _trendingMovies: MutableState<TrendingMoviesState> = mutableStateOf(TrendingMoviesState())
    val topRatedMovies: State<MovieState> = _topRatedMovies
    val res: State<MovieState> = _res
    val movieDetail: State<MovieDetailState> = _movieDetail
    val movieLink: State<VideoLinkState> = _movieLink
    val trendingMovies: State<TrendingMoviesState> = _trendingMovies

    fun getMovies(pageNumber:Int? = 1) {
        viewModelScope.launch {
            useCase.getMovies(pageNumber?: 1)
                .doOnSuccess { _res.value = MovieState(data = it!!) }
                .doOnFailure { _res.value = MovieState(error = it?.message.toString()) }
                .doOnLoading { _res.value = MovieState(isLoading = true) }.collect()
        }
    }

    fun getMovieDetails(movieId: String) {
        viewModelScope.launch {
            useCase.getMovieDetails(movieId)
                .filterNotNull()
                .doOnSuccess { movieDetail -> _movieDetail.value = MovieDetailState(data = movieDetail) }
                .doOnFailure { _movieDetail.value = MovieDetailState(error = it?.message.toString()) }
                .doOnLoading { _movieDetail.value = MovieDetailState(isLoading = true) }.collect()
        }
    }
    fun getVideoLink(movieId: Int) {
        viewModelScope.launch {
            useCase.getVideoLink(movieId)
                .filterNotNull()
                .doOnSuccess { movieDetail -> _movieLink.value = VideoLinkState(data = movieDetail) }
                .doOnFailure { _movieLink.value = VideoLinkState(error = it?.message.toString()) }
                .doOnLoading { _movieLink.value = VideoLinkState(isLoading = true) }.collect()
        }
    }
    fun getTopRatedMovies(pageNumber: Int? = 1) {
        viewModelScope.launch {
            useCase.getTopRatedMovies(pageNumber = pageNumber?: 1)
                .doOnSuccess { _topRatedMovies.value = MovieState(data = it!!) }
                .doOnFailure { _topRatedMovies.value = MovieState(error = it?.message.toString()) }
                .doOnLoading { _topRatedMovies.value = MovieState(isLoading = true) }.collect()
        }
    }
    fun getTrendingMovies() {
        viewModelScope.launch {
            useCase.getTrendingMovies()
                .filterNotNull()
                .doOnSuccess { movieDetail ->
                    _trendingMovies.value = TrendingMoviesState(data = movieDetail)
                    Extensions.mLogs(movieDetail.toString())
                }
                .doOnFailure {
                    _trendingMovies.value = TrendingMoviesState(error = it?.message.toString())
                    Extensions.mLogs(it?.message.toString())
                }
                .doOnLoading { _trendingMovies.value = TrendingMoviesState(isLoading = true) }.collect()
        }
    }
}

data class MovieState(
    val data: List<Movies.Results> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

data class MovieDetailState(
    val data: MovieDetail = MovieDetail(),
    val error: String = "",
    val isLoading: Boolean = false
)
data class VideoLinkState(
    val data: VideoPlayAbleLink = VideoPlayAbleLink(),
    val error: String = "",
    val isLoading: Boolean = false
)
data class TrendingMoviesState(
    val data: TrendingMovies = TrendingMovies(),
    val error: String = "",
    val isLoading: Boolean = false

)
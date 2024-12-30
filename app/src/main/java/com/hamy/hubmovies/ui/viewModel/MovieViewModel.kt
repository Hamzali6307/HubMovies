package com.hamy.hubmovies.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamy.hubmovies.common.doOnFailure
import com.hamy.hubmovies.common.doOnLoading
import com.hamy.hubmovies.common.doOnSuccess
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.features.movies.domain.useCase.MovieUseCase
import com.hamy.hubmovies.utils.Extensions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val useCase: MovieUseCase):ViewModel() {
    private val _res: MutableState<MovieState> = mutableStateOf(MovieState())
    val res: State<MovieState> = _res

    init {
        viewModelScope.launch {
            useCase.getMovies()
                .doOnSuccess {
                    _res.value = MovieState(data = it!!)
                    Extensions.myLogs(it.toString())
                }
                .doOnFailure {
                    _res.value = MovieState(error = it?.message.toString())
                }
                .doOnLoading {
                    _res.value = MovieState(isLoading = true)
                }.collect()
        }
    }
}
data class MovieState(
    val data: List<Movies.Results> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)
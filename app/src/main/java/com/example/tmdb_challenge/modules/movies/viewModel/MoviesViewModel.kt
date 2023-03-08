package com.example.tmdb_challenge.modules.movies.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tmdb_challenge.base.BaseViewModel
import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.usecases.movies.GetPopularUseCase
import com.example.tmdb_challenge.usecases.movies.GetRecommendedMoviesUseCase
import com.example.tmdb_challenge.usecases.movies.GetTopMoviesUseCase
import com.example.tmdb_challenge.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getTopRatedMoviesUseCase: GetTopMoviesUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getRecommendedMoviesUseCase: GetRecommendedMoviesUseCase
) : BaseViewModel() {
    private val moviesPopularMLD = MutableLiveData<List<Movie>>()
    private val moviesTopRatedMLD = MutableLiveData<List<Movie>>()
    private val moviesRecommendedMLD = MutableLiveData<List<Movie>>()
    private val errorLoadingDataMLD = MutableLiveData<Event<Unit>>()
    private val country = Locale.getDefault().country

    init {
        loadTopRated(1)
        loadPopular(1)
        loadRecommended(1)
    }


    fun loadTopRated(currentPage: Int) {
        viewModelScope.launch {
            getTopRatedMoviesUseCase.invoke(currentPage)
                .onEmpty {
                    errorLoadingDataMLD.value = Event(Unit)
                }
                .collect { movies ->
                    moviesTopRatedMLD.value = moviesTopRatedMLD.value?.plus(movies) ?: movies
                }
        }
    }

    fun loadPopular(currentPage: Int) {
        viewModelScope.launch {
            getPopularUseCase.invoke(currentPage)
                .onEmpty {
                    errorLoadingDataMLD.value = Event(Unit)
                }
                .collect { movies ->
                    moviesPopularMLD.value = moviesPopularMLD.value?.plus(movies) ?: movies
                }
        }
    }

    fun loadRecommended(currentPage: Int) {
        viewModelScope.launch {
            getRecommendedMoviesUseCase.invoke(currentPage, country)
                .onEmpty {
                    errorLoadingDataMLD.value = Event(Unit)
                }
                .collect { movies ->
                    moviesRecommendedMLD.value = moviesRecommendedMLD.value?.plus(movies) ?: movies
                }
        }
    }
    fun moviesPopular(): LiveData<List<Movie>> = moviesPopularMLD
    fun moviesTopRated(): LiveData<List<Movie>> = moviesTopRatedMLD
    fun moviesRecommended(): LiveData<List<Movie>> = moviesRecommendedMLD
    fun errorLoadingData(): LiveData<Event<Unit>> = errorLoadingDataMLD

}
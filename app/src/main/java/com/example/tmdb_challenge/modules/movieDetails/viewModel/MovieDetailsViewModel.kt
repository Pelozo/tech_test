package com.example.tmdb_challenge.modules.movieDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tmdb_challenge.base.BaseViewModel
import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.modules.movieDetails.constants.MovieDetailsConstants.Companion.YOUTUBE_URL
import com.example.tmdb_challenge.usecases.movies.GetMovieTrailerUseCase
import com.example.tmdb_challenge.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase
) : BaseViewModel() {

    private val movieMLD = MutableLiveData<Movie>()
    private val movieTrailerMLD = MutableLiveData<String>()
    private val trailerLoadedMLD = MutableLiveData<Event<Boolean>>()


    fun setUp(movie: Movie) {
        movieMLD.value = movie
        viewModelScope.launch {
            getMovieTrailer(movie.movieId.toString())
        }
    }

    private suspend fun getMovieTrailer(movieId: String) {
        val trailerId = getMovieTrailerUseCase.invoke(movieId)
        if (trailerId != null) {
            movieTrailerMLD.value = "$YOUTUBE_URL$trailerId"
            trailerLoadedMLD.value = Event(true)
        } else {
            trailerLoadedMLD.value = Event(false)
        }
    }


    fun movie(): LiveData<Movie> = movieMLD
    fun movieTrailerUrl(): String? = movieTrailerMLD.value
    fun movieTrailerLoaded(): LiveData<Event<Boolean>> = trailerLoadedMLD
}
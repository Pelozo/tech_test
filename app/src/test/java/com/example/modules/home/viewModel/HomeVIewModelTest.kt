package com.example.modules.home.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.modules.movies.viewModel.MoviesViewModel
import com.example.tmdb_challenge.usecases.movies.GetPopularUseCase
import com.example.tmdb_challenge.usecases.movies.GetRecommendedMoviesUseCase
import com.example.tmdb_challenge.usecases.movies.GetTopMoviesUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeVIewModelTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getTopMoviesUseCase: GetTopMoviesUseCase
    @Mock
    lateinit var getPopularUseCase: GetPopularUseCase
    @Mock
    lateinit var getRecommendedMoviesUseCase: GetRecommendedMoviesUseCase

    lateinit var viewmodel: MoviesViewModel

    val movieList = listOf(
        Movie (
            movieId = 10L,
            backdropPath = "path",
            originalLanguage = "en",
            title = "movieTitle",
            overview = "It's a movie",
            posterPath = "poster/path",
            releaseDate = "2019-05-09",
            voteAverage = 1.5f,
            list = "TOP_RATED"
        ),
        Movie (
            movieId = 20L,
            backdropPath = "path",
            originalLanguage = "es",
            title = "movieTitle",
            overview = "It's a movie",
            posterPath = "poster/path",
            releaseDate = "2019-05-09",
            voteAverage = 1.5f,
            list = "TOP_RATED"
        ),
        Movie (
            movieId = 30L,
            backdropPath = "path",
            originalLanguage = "en",
            title = "movieTitle",
            overview = "It's a movie",
            posterPath = "poster/path",
            releaseDate = "2019-05-09",
            voteAverage = 1.5f,
            list = "TOP_RATED"
        ),
        Movie (
            movieId = 40L,
            backdropPath = "path",
            originalLanguage = "ja",
            title = "movieTitle",
            overview = "It's a movie",
            posterPath = "poster/path",
            releaseDate = "2019-05-09",
            voteAverage = 1.5F,
            list = "TOP_RATED"
        )
    )
    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        viewmodel = MoviesViewModel(getTopMoviesUseCase, getPopularUseCase, getRecommendedMoviesUseCase)
    }

    @After
    fun after(){
        Mockito.validateMockitoUsage();
    }

    @Test
    fun `moviesRecommended returns empty list successfully`() = runBlocking {
        //given
        Mockito.`when`(getRecommendedMoviesUseCase.invoke(1, "es")).thenReturn(flow {emit(movieList)})

        //when

        //then
        assertEquals(
            emptyList<Movie>(),
            viewmodel.moviesRecommended().value
        )
    }



}

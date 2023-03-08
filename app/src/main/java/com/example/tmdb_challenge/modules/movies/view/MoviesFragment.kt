package com.example.tmdb_challenge.modules.movies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb_challenge.MainActivity
import com.example.tmdb_challenge.base.BaseFragment
import com.example.tmdb_challenge.databinding.FragmentMoviesBinding
import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.modules.movies.view.adapters.MovieAdapter
import com.example.tmdb_challenge.modules.movies.viewModel.MoviesViewModel
import com.example.tmdb_challenge.util.EndlessRecyclerOnScrollListener
import com.example.tmdb_challenge.util.observe
import com.example.tmdb_challenge.util.observeEvent
import com.example.tmdb_challenge.util.withViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>() {

    private val popularAdapter = MovieAdapter { movie ->
        navigateToDetails(movie)
    }

    private val topRatedAdapter = MovieAdapter { movie ->
        navigateToDetails(movie)
    }

    private val recommendedAdapter = MovieAdapter { movie ->
        navigateToDetails(movie)
    }

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMoviesBinding = FragmentMoviesBinding.inflate(inflater, container, false)

    override fun initView() {
        super.initView()
        with(binding) {
            rvPopular.apply {
                adapter = popularAdapter
                layoutManager =
                    GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
                addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager as GridLayoutManager) {
                    override fun onLoadMore(currentPage: Int) {
                        getViewModel().loadPopular(currentPage)
                    }
                })
            }

            rvTopRated.apply {
                adapter = topRatedAdapter
                layoutManager =
                    GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
                addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager as GridLayoutManager) {
                    override fun onLoadMore(currentPage: Int) {
                        getViewModel().loadTopRated(currentPage)
                    }
                })
            }

            rvRecommended.apply {
                adapter = recommendedAdapter
                layoutManager =
                    GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
                addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager as GridLayoutManager) {
                    override fun onLoadMore(currentPage: Int) {
                        getViewModel().loadRecommended(currentPage)
                    }
                })
            }
        }
    }

    override fun getViewModel(): MoviesViewModel = withViewModel {
        observe(moviesPopular()) { onMoviesPopular(it) }
        observe(moviesTopRated()) { onMoviesTopRated(it) }
        observe(moviesRecommended()) { onMoviesRecommended(it) }
        observeEvent(errorLoadingData()){ showErrorLoadingData() }
    }

    private fun showErrorLoadingData() {
        (activity as MainActivity).showErrorDialog()
    }

    private fun onMoviesPopular(movies: List<Movie>) {
        popularAdapter.submitList(movies)
    }

    private fun onMoviesTopRated(movies: List<Movie>) {
        topRatedAdapter.submitList(movies)
    }

    private fun onMoviesRecommended(movies: List<Movie>) {
        recommendedAdapter.submitList(movies)
    }

    private fun navigateToDetails(movie: Movie) {
        findNavController().navigate(
            MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(
                movie
            )
        )
    }
}
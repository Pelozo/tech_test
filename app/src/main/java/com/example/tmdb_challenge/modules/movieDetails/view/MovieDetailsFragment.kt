package com.example.tmdb_challenge.modules.movieDetails.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tmdb_challenge.R
import com.example.tmdb_challenge.base.BaseFragment
import com.example.tmdb_challenge.databinding.FragmentMovieDetailsBinding
import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.modules.movieDetails.viewModel.MovieDetailsViewModel
import com.example.tmdb_challenge.util.Constants
import com.example.tmdb_challenge.util.observe
import com.example.tmdb_challenge.util.observeEvent
import com.example.tmdb_challenge.util.withViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment<FragmentMovieDetailsBinding>() {

    private val args: MovieDetailsFragmentArgs by navArgs()


    override fun setUp(arguments: Bundle?) {
        super.setUp(arguments)
        getViewModel().setUp(args.movie)
    }

    override fun initView() {
        super.initView()
        with(binding) {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btTrailer.setOnClickListener {
                launchTrailer()
            }
        }
    }

    override fun getViewModel(): MovieDetailsViewModel = withViewModel {
        observe(movie()) { onMovie(it) }
        observeEvent(movieTrailerLoaded()) { onMovieTrailerLoaded(it) }
    }

    private fun onMovieTrailerLoaded(loaded: Boolean) {
        binding.pbLoadingTrailer.isVisible = false
        if (loaded){
            binding.btTrailer.text = getString(R.string.watch_trailer)
            binding.btTrailer.isClickable = true
        } else {
            binding.btTrailer.text = getString(R.string.watch_trailer_error)
        }
    }

    private fun launchTrailer() {
        getViewModel().movieTrailerUrl()?.let { url ->
            val intent = CustomTabsIntent.Builder()
                .setUrlBarHidingEnabled(true)
                .build()
            intent.launchUrl(requireContext(), Uri.parse(url))
        }
    }

    private fun onMovie(movie: Movie) {
        with(binding) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview

            btnRating.text = movie.voteAverage.toString()
            btnLanguage.text = movie.originalLanguage
            btnYear.text = movie.releaseDate.substringBefore("-")

            Glide
                .with(requireContext())
                .load("${Constants.IMAGE_URL}${movie.posterPath}")
                .centerCrop()
                .apply(Constants.glideOptions)
                .into(binding.ivPoster)
        }
    }

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMovieDetailsBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

}
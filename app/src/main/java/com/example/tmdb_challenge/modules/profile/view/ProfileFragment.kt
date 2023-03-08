package com.example.tmdb_challenge.modules.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tmdb_challenge.MainActivity
import com.example.tmdb_challenge.R
import com.example.tmdb_challenge.base.BaseFragment
import com.example.tmdb_challenge.databinding.FragmentProfileBinding
import com.example.tmdb_challenge.domain.models.Profile
import com.example.tmdb_challenge.modules.profile.constants.ProfileConstants.Companion.DEFAULT_PROFILE_URL
import com.example.tmdb_challenge.modules.profile.view.adapters.RatedMovieAdapter
import com.example.tmdb_challenge.modules.profile.viewModel.ProfileViewModel
import com.example.tmdb_challenge.util.Constants
import com.example.tmdb_challenge.util.observe
import com.example.tmdb_challenge.util.observeEvent
import com.example.tmdb_challenge.util.withViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val ratedMoviesAdapter = RatedMovieAdapter()

    override fun initView() {
        super.initView()
        with(binding) {
            rvRatedMovies.apply {
                adapter = ratedMoviesAdapter
                layoutManager =
                    GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun getViewModel(): ProfileViewModel = withViewModel {
        observe(profileLoaded()) { showProfile(it) }
        observeEvent(errorLoadingProfile()) { showErrorLoading() }
    }

    private fun showProfile(profile: Profile) {
        with(binding){
            tvUser.text = getString(R.string.welcome_message, profile.user)

            Glide
                .with(requireContext())
                .load(DEFAULT_PROFILE_URL)
                .centerCrop()
                .apply(Constants.glideOptions)
                .into(binding.ivProfile)
        }


        ratedMoviesAdapter.submitList(profile.ratedMovies)
    }

    private fun showErrorLoading() {
        (activity as MainActivity).showErrorDialog()
    }



}
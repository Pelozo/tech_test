package com.example.tmdb_challenge.modules.profile.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.tmdb_challenge.R
import com.example.tmdb_challenge.databinding.ItemRatedMovieBinding
import com.example.tmdb_challenge.domain.models.RatedMovie
import com.example.tmdb_challenge.util.Constants.Companion.IMAGE_URL
import com.example.tmdb_challenge.util.Constants.Companion.glideOptions

class RatedMovieAdapter(
) : ListAdapter<RatedMovie, RatedMovieAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatedMovieAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rated_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatedMovieAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemRatedMovieBinding.bind(view)

        fun bind(item: RatedMovie, pos: Int) = with(binding) {
            Glide
                .with(ivPoster.context)
                .load("$IMAGE_URL${item.posterPath}")
                .apply(glideOptions)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(12)))
                .into(ivPoster)
            btnRating.text = item.rate.toString()
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<RatedMovie>() {
    override fun areItemsTheSame(oldItem: RatedMovie, newItem: RatedMovie): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: RatedMovie, newItem: RatedMovie): Boolean {
        return oldItem == newItem
    }
}
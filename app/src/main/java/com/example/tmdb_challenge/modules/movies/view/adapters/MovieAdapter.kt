package com.example.tmdb_challenge.modules.movies.view.adapters


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
import com.example.tmdb_challenge.databinding.ItemMovieBinding
import com.example.tmdb_challenge.domain.models.Movie
import com.example.tmdb_challenge.util.Constants.Companion.IMAGE_URL
import com.example.tmdb_challenge.util.Constants.Companion.glideOptions

class MovieAdapter(
    private val onClick: ((item: Movie) -> Unit)
) : ListAdapter<Movie, MovieAdapter.ViewHolder>(DiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun addItems(items: List<Movie>) {
        submitList(items)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemMovieBinding.bind(view)

        fun bind(item: Movie, pos: Int) = with(binding) {
            Glide
                .with(ivPoster.context)
                .load("$IMAGE_URL${item.posterPath}")
                .apply(glideOptions)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(12)))
                .into(ivPoster)

            ivPoster.setOnClickListener {
                onClick(item)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
package com.example.tmdb_challenge.util

import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tmdb_challenge.R


class Constants {

    companion object{
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

        val glideOptions: RequestOptions = RequestOptions()
            .placeholder(R.drawable.progress_anim)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
    }

}
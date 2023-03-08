package com.example.tmdb_challenge.interceptors

import com.example.tmdb_challenge.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * intercepts request to add api key *
 */
class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val url = req.url().newBuilder().addQueryParameter("api_key", BuildConfig.TMDB_API).build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}
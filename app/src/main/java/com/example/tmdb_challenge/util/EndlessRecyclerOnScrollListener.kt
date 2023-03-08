package com.example.tmdb_challenge.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener(linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold = 5
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    private var currentPage = 1

    private val linearLayoutManager: LinearLayoutManager

    init {
        this.linearLayoutManager = linearLayoutManager
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.getChildCount()
        totalItemCount = linearLayoutManager.getItemCount()
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }
    abstract fun onLoadMore(currentPage: Int)
}
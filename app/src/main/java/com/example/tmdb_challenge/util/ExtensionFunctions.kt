package com.example.tmdb_challenge.util

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.tmdb_challenge.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

inline fun <reified T : ViewModel, B : ViewBinding> BaseFragment<B>.retrieveViewModel(): T {
    val viewModel: T by viewModels()
    return viewModel
}

inline fun <reified T : ViewModel, B : ViewBinding> BaseFragment<B>.withViewModel(body: T.() -> Unit): T {
    return if (baseViewModel == null) {
        val vm = retrieveViewModel<T, B>()
        vm.body()
        vm
    } else {
        baseViewModel as T
    }
}
fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(livedata: L, body: (T) -> Unit) {
    livedata.observe(this, Observer(body))
}

fun <T : Any, L : LiveData<Event<T>>> LifecycleOwner.observeEvent(livedata: L, body: (T) -> Unit) {
    livedata.observe(this, EventObserver { body.invoke(it) })
}

inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}
fun Long.toDate(): String? =
    try {
        val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        val netDate = Date(this)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }

package com.example.tmdb_challenge.modules.profile.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tmdb_challenge.base.BaseViewModel
import com.example.tmdb_challenge.domain.models.Profile
import com.example.tmdb_challenge.usecases.profile.GetProfileUseCase
import com.example.tmdb_challenge.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : BaseViewModel() {

    private val profileMLD = MutableLiveData<Profile>()
    private val errorLoadingMLD = MutableLiveData<Event<Unit>>()

    init {
        getProfile()
    }

    private fun getProfile(){
        viewModelScope.launch {
            val profile = getProfileUseCase.invoke()
            profile?.let {
                profileMLD.value = it
            } ?: run {
                errorLoadingMLD.value = Event(Unit)
            }
        }
    }

    fun profileLoaded(): LiveData<Profile> = profileMLD
    fun errorLoadingProfile(): LiveData<Event<Unit>> = errorLoadingMLD

}
package com.example.tmdb_challenge.modules.locations.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tmdb_challenge.base.BaseViewModel
import com.example.tmdb_challenge.domain.models.firestore.LocationFirestore
import com.example.tmdb_challenge.usecases.location.GetPastLocationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val getPastLocationsUseCase: GetPastLocationsUseCase
) : BaseViewModel() {

    private val locationsMLD = MutableLiveData<List<LocationFirestore>>()


    fun requestLocations(){
        viewModelScope.launch {
            getPastLocationsUseCase.invoke()
                .collect{
                    locationsMLD.value = it
                }
        }
    }
    fun locations(): LiveData<List<LocationFirestore>> = locationsMLD
}
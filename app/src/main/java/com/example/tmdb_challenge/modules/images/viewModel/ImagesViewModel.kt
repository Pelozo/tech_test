package com.example.tmdb_challenge.modules.images.viewModel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tmdb_challenge.base.BaseViewModel
import com.example.tmdb_challenge.usecases.image.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val uploadImageUseCase: UploadImageUseCase
): BaseViewModel() {

    private val uploadingMLD = MutableLiveData<Boolean>()
    private val uploadStatusMLD = MutableLiveData<Boolean>()

    fun uploadImage(bitmap: Bitmap){
        viewModelScope.launch {
            uploadingMLD.value = true
            val isSuccessful = uploadImageUseCase.invoke(bitmap).await()
            uploadStatusMLD.value = isSuccessful
            uploadingMLD.value = false
        }
    }

    fun uploadImage(uri: Uri){
        viewModelScope.launch {
            uploadingMLD.value = true
            val isSuccessful = uploadImageUseCase.invoke(uri).await()
            uploadStatusMLD.value = isSuccessful
            uploadingMLD.value = false
        }
    }

    fun isUploading(): LiveData<Boolean> = uploadingMLD
    fun uploadStatus(): LiveData<Boolean> = uploadStatusMLD

}
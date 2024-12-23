package com.podcastapp.profile.ui.navigation.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.podcastapp.profile.domain.model.UserFull
import com.podcastapp.profile.domain.use_cases.EditProfileUseCase
import com.podcastapp.profile.domain.use_cases.PurchasePremiumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileUseCase: EditProfileUseCase,
    private val purchasePremiumUseCase: PurchasePremiumUseCase,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    private val _state = MutableStateFlow(UiStateHolder<UserFull>())
    val state: StateFlow<UiStateHolder<UserFull>> get() = _state

    private val _imageFile = MutableLiveData<File?>()
    val imageFile: LiveData<File?> = _imageFile

    fun editProfile(username:String, image:File) = viewModelScope.launch {
        profileUseCase(username, image).collect { it ->
            when (it) {
                is UiEvent.Loading -> {
                    _state.value = UiStateHolder(isLoading = true)
                }
                is UiEvent.Success -> {
                    _state.value = UiStateHolder(isSuccess = true)
                }
                is UiEvent.Error -> {
                    _state.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }
    }

    fun onPurchasePremium(cvv:String, cardNumber:String, expirationDate:String, cardHolder:String) = viewModelScope.launch {
        purchasePremiumUseCase(cvv, cardNumber,expirationDate,cardHolder).collect { it ->
            when (it) {
                is UiEvent.Loading -> {
                    _state.value = UiStateHolder(isLoading = true)
                }
                is UiEvent.Success -> {
                    _state.value = UiStateHolder(isSuccess = true)
                }
                is UiEvent.Error -> {
                    _state.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }
    }

    fun onImageSelected(uri: Uri) {
        _imageFile.value = uriToFile(uri, appContext)
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("temp", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream?.copyTo(output)
        }
        return tempFile
    }
}
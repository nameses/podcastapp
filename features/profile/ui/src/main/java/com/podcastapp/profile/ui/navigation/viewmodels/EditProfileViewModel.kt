package com.podcastapp.profile.ui.navigation.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.RepoEvent
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.podcastapp.profile.domain.model.UserFull
import com.podcastapp.profile.domain.repo.UserRepository
import com.podcastapp.profile.domain.use_cases.EditProfileUseCase
import com.podcastapp.profile.domain.use_cases.ProfileUseCase
import com.podcastapp.profile.domain.use_cases.PurchasePremiumUseCase
import com.podcastapp.profile.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileEditUseCase: EditProfileUseCase,
    private val userRepository: UserRepository,
    private val purchasePremiumUseCase: PurchasePremiumUseCase,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    private val _state = MutableStateFlow(UiStateHolder<UserFull>())
    val state: StateFlow<UiStateHolder<UserFull>> get() = _state

    var initialUsername = "";
    val usernameState: MutableStateFlow<String> = MutableStateFlow(String())

    private val _editState = MutableStateFlow(UiStateHolder<Unit>())
    val editState: StateFlow<UiStateHolder<Unit>> get() = _editState

    private val _premiumPurchaseState = MutableStateFlow(UiStateHolder<Unit>())
    val premiumPurchaseState: StateFlow<UiStateHolder<Unit>> get() = _premiumPurchaseState

    private val _imageFile = MutableLiveData<File?>()
    val imageFile: LiveData<File?> = _imageFile

    init {
        loadUser()
        initImageFile()

    }

    private fun initImageFile() {
        if (_state.value.isSuccess && _state.value.data!!.imageUrl != null) {
            _imageFile.value = _state.value.data!!.imageUrl?.let { File(it) }
        }
    }

    fun setUsernameState(username: String) {
        usernameState.value = username
    }

    fun loadUser() = viewModelScope.launch {
        _state.value = UiStateHolder(isLoading = true)

        when (val response = userRepository.getProfile()) {
            is RepoEvent.Success -> {
                _state.value = UiStateHolder(isSuccess = true, data = response.data)
                usernameState.value = response.data!!.username
                initialUsername = _state.value.data!!.username
            }

            is RepoEvent.Error -> {
                _state.value =
                    UiStateHolder(message = response.message.toString(), errors = response.errors)
            }
        }
    }

    fun editProfile(username: String?, image: File?) = viewModelScope.launch {
        var usernameProcessed = username
        var imageProcessed = image
        if (image == null) {
            val resourceId = R.drawable.default_avatar
            val bitmap = getBitmapFromResource(appContext, resourceId)

            imageProcessed = saveBitmapToFile(appContext, bitmap, "temp.png")
        }
        if(initialUsername == usernameProcessed) {
            usernameProcessed = null
        }
        profileEditUseCase(usernameProcessed, imageProcessed!!).collect { it ->
            when (it) {
                is UiEvent.Loading -> {
                    _editState.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    _editState.value = UiStateHolder(isSuccess = true)
                }

                is UiEvent.Error -> {
                    _editState.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }
    }

    fun onPurchasePremium(
        cvv: String, cardNumber: String, expirationDate: String, cardHolder: String
    ) = viewModelScope.launch {
        purchasePremiumUseCase(cvv, cardNumber, expirationDate, cardHolder).collect { it ->
            when (it) {
                is UiEvent.Loading -> {
                    _premiumPurchaseState.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    _premiumPurchaseState.value = UiStateHolder(isSuccess = true)
                }

                is UiEvent.Error -> {
                    _premiumPurchaseState.value =
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
            ?: throw IllegalArgumentException("Unable to open input stream for URI: $uri")

        val tempFile = File.createTempFile("temp", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream.use { input ->
                input.copyTo(output)
            }
        }
        return tempFile
    }


    private fun getBitmapFromResource(context: Context, resourceId: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, resourceId)
    }

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String): File {
        val file = File(context.cacheDir, filename)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file
    }
}
package com.features.auth.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.model.UiEvent
import com.core.common.model.UiStateHolder
import com.core.common.services.TokenManager
import com.features.auth.domain.model.AuthData
import com.features.auth.domain.use_cases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _authState = MutableStateFlow(UiStateHolder<AuthData>())
    val authState: StateFlow<UiStateHolder<AuthData>> get() = _authState

    fun login(email: String, password: String) = viewModelScope.launch {

        loginUseCase(email, password).collect { it ->
            when (it) {
                is UiEvent.Loading -> {
                    _authState.value = UiStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    if (it.data?.success == true) {
                        it.data?.token?.let { tokenManager.saveToken(it) }
                    }
                    _authState.value = UiStateHolder(isSuccess = true, data = it.data)
                }

                is UiEvent.Error -> {
                    _authState.value =
                        UiStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }

    }
}
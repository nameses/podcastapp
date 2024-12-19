package com.features.auth.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.UiEvent
import com.features.auth.domain.use_cases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    ViewModel() {
    private val _authState = MutableStateFlow(AuthStateHolder())
    val authState: StateFlow<AuthStateHolder> get() = _authState

    fun register(username: String, email: String, password: String) = viewModelScope.launch {

        registerUseCase(username, email, password).collect {
            when (it) {
                is UiEvent.Loading -> {
                    _authState.value = AuthStateHolder(isLoading = true)
                }

                is UiEvent.Success -> {
                    _authState.value = AuthStateHolder(data = it.data)
                }

                is UiEvent.Error -> {
                    _authState.value =
                        AuthStateHolder(message = it.message.toString(), errors = it.errors)
                }
            }
        }
    }
}
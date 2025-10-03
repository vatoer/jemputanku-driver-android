package id.stargan.jemputankudriver.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.stargan.jemputankudriver.core.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun signInWithGoogle(idToken: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _loginSuccess.value = false
        viewModelScope.launch {
            val result = authRepository.signInWithGoogleCredential(idToken)
            _isLoading.value = false
            if (result.isSuccess) {
                _loginSuccess.value = true
            } else {
                _errorMessage.value = result.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun resetState() {
        _isLoading.value = false
        _errorMessage.value = null
        _loginSuccess.value = false
    }

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()
    fun signOut() = authRepository.signOut()
}


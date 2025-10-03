package id.stargan.jemputankudriver.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.stargan.jemputankudriver.core.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun login(email: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            val result = authRepository.signInWithEmail(email, password)
            _isLoading.value = false
            if (result.isSuccess) {
                _loginSuccess.value = true
            } else {
                _errorMessage.value = result.exceptionOrNull()?.localizedMessage
            }
        }
    }

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()
}

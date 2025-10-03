package id.stargan.jemputankudriver.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import id.stargan.jemputankudriver.core.data.AuthRepository

class SignupViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val authRepository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _signupSuccess = MutableStateFlow(false)
    val signupSuccess: StateFlow<Boolean> = _signupSuccess

    fun signup(name: String, email: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _signupSuccess.value = false
        viewModelScope.launch {
            val result = authRepository.signupWithEmail(name, email, password)
            _isLoading.value = false
            if (result.isSuccess) {
                _signupSuccess.value = true
            } else {
                _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Signup gagal. Coba lagi."
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

package id.stargan.jemputankudriver.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun loginWithEmail(email: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _loginSuccess.value = false
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        _loginSuccess.value = true
                    } else {
                        val exception = task.exception
                        _errorMessage.value = when (exception) {
                            is FirebaseAuthInvalidUserException -> "Email tidak ditemukan."
                            is FirebaseAuthInvalidCredentialsException -> "Password salah."
                            else -> exception?.localizedMessage ?: "Login gagal. Coba lagi."
                        }
                    }
                }
        }
    }

    fun loginWithGoogle(idToken: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _loginSuccess.value = false
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        _loginSuccess.value = true
                    } else {
                        _errorMessage.value = task.exception?.localizedMessage ?: "Login Google gagal."
                    }
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}


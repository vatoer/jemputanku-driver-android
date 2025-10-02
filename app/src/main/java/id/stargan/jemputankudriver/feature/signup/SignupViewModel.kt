package id.stargan.jemputankudriver.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        // Simpan nama ke profile user jika perlu
                        val user = auth.currentUser
                        user?.updateProfile(com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name).build())
                        _signupSuccess.value = true
                    } else {
                        val exception = task.exception
                        _errorMessage.value = when (exception) {
                            is FirebaseAuthUserCollisionException -> "Email sudah digunakan, silakan login atau gunakan email lain."
                            else -> exception?.localizedMessage ?: "Signup gagal. Coba lagi."
                        }
                    }
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}


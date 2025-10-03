package id.stargan.jemputankudriver.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.stargan.jemputankudriver.core.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _displayName = MutableStateFlow<String?>(authRepository.currentUser?.displayName)
    val displayName: StateFlow<String?> = _displayName

    private val _email = MutableStateFlow<String?>(authRepository.currentUser?.email)
    val email: StateFlow<String?> = _email

    init {
        viewModelScope.launch {
            authRepository.authStateFlow().collect { user ->
                _displayName.value = user?.displayName
                _email.value = user?.email
            }
        }
    }

    fun logout() {
        authRepository.signOut()
    }
}

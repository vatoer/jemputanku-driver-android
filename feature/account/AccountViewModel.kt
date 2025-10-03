package id.stargan.jemputankudriver.feature.account

import androidx.lifecycle.ViewModel
import id.stargan.jemputankudriver.core.data.AuthRepository

class AccountViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    fun logout() {
        authRepository.signOut()
    }
}


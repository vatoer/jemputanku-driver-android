package id.stargan.jemputankudriver.core.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()) {
    val currentUser get() = firebaseAuth.currentUser

    suspend fun signInWithEmail(email: String, password: String): Result<AuthResult> =
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null
}


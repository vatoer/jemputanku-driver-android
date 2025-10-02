package id.stargan.jemputankudriver.driver.navigation

import android.app.Application
import android.util.Log
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberSnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.stargan.jemputankudriver.core.data.OnboardingPreferences
import id.stargan.jemputankudriver.driver.ui.main.MainScreen
import id.stargan.jemputankudriver.feature.active_trip.ActiveTripScreen
import id.stargan.jemputankudriver.feature.onboarding.OnboardingScreen
import id.stargan.jemputankudriver.feature.login.LoginScreen
import id.stargan.jemputankudriver.feature.signup.SignupScreen
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.tasks.await
import android.content.Context

// Definisikan rute (alamat unik) untuk setiap layar
object AppRoutes {
    const val MAIN_SCREEN = "main"
    const val ACTIVE_TRIP_SCREEN = "active_trip"
    const val ONBOARDING_SCREEN = "onboarding"
    const val LOGIN_SCREEN = "login"
    const val SIGNUP_SCREEN = "signup"
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current.applicationContext as Application
    val navController = rememberNavController()
    val onboardingShownFlow = OnboardingPreferences.isOnboardingShown(context)
    val isOnboardingShown by onboardingShownFlow.collectAsState(initial = false)
    var startDestination by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val firebaseAuth = FirebaseAuth.getInstance()
    var googleSignInError by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = rememberSnackbarHostState()

    suspend fun signInWithGoogleCredentialManager(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val credentialManager = CredentialManager.create(context)
        val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        val webClientId = if (resId != 0) context.getString(resId) else ""
        Log.d("GoogleSignIn", "webClientId: $webClientId")
        if (webClientId.isBlank()) {
            onError("default_web_client_id is missing or empty. Cek strings.xml dan Firebase Console.")
            return
        }
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        try {
            Log.d("GoogleSignIn", "Launching CredentialManager.getCredential...")
            val response: GetCredentialResponse = credentialManager.getCredential(context, request)
            val googleCredential = response.credential
            val idToken = googleCredential.data.getString("googleIdToken")
            Log.d("GoogleSignIn", "idToken: $idToken")
            if (idToken != null) {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(credential).await()
                onSuccess()
            } else {
                onError("Google ID token is null.")
            }
        } catch (e: GetCredentialException) {
            Log.e("GoogleSignIn", "GetCredentialException: ${e.localizedMessage}", e)
            onError("Google sign-in failed: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Exception: ${e.localizedMessage}", e)
            onError("Firebase authentication failed: ${e.localizedMessage}")
        }
    }

    LaunchedEffect(googleSignInError) {
        googleSignInError?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(isOnboardingShown) {
        startDestination = if (isOnboardingShown) AppRoutes.LOGIN_SCREEN else AppRoutes.ONBOARDING_SCREEN
    }

    if (startDestination != null) {
        NavHost(navController = navController, startDestination = startDestination!!) {
            composable(route = AppRoutes.ONBOARDING_SCREEN) {
                OnboardingScreen(
                    onFinish = {
                        scope.launch {
                            OnboardingPreferences.setOnboardingShown(context, true)
                            navController.navigate(AppRoutes.LOGIN_SCREEN) {
                                popUpTo(AppRoutes.ONBOARDING_SCREEN) { inclusive = true }
                            }
                        }
                    },
                    onReset = {
                        scope.launch {
                            OnboardingPreferences.clear(context)
                        }
                    }
                )
            }
            composable(route = AppRoutes.LOGIN_SCREEN) {
                // Tampilkan Snackbar untuk error
                SnackbarHost(hostState = snackbarHostState)
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AppRoutes.MAIN_SCREEN) {
                            popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                        }
                    },
                    onGoogleLogin = {
                        Log.d("GoogleSignIn", "Tombol Google Sign-In diklik")
                        scope.launch {
                            signInWithGoogleCredentialManager(
                                context = context,
                                onSuccess = {
                                    navController.navigate(AppRoutes.MAIN_SCREEN) {
                                        popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                                    }
                                },
                                onError = { errorMsg ->
                                    googleSignInError = errorMsg
                                }
                            )
                        }
                    },
                    onNavigateToSignup = {
                        navController.navigate(AppRoutes.SIGNUP_SCREEN) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = AppRoutes.SIGNUP_SCREEN) {
                SignupScreen(
                    onSignupSuccess = {
                        navController.navigate(AppRoutes.MAIN_SCREEN) {
                            popUpTo(AppRoutes.SIGNUP_SCREEN) { inclusive = true }
                        }
                    },
                    onGoogleSignup = {
                        // TODO: Integrasi Google Sign-Up
                    },
                    onNavigateToLogin = {
                        navController.popBackStack(AppRoutes.LOGIN_SCREEN, inclusive = false)
                    }
                )
            }
            // Tujuan 1: Layar Utama dengan Bottom Nav
            composable(route = AppRoutes.MAIN_SCREEN) {
                MainScreen(navController = navController)
            }

            // Tujuan 2: Layar Perjalanan Aktif (Peta)
            composable(route = AppRoutes.ACTIVE_TRIP_SCREEN) {
                ActiveTripScreen(navController = navController)
            }
        }
    }
}
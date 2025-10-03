package id.stargan.jemputankudriver.driver.navigation

import android.app.Application
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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import id.stargan.jemputankudriver.core.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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
    val authViewModel: AuthViewModel = viewModel()

    // Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                authViewModel.signInWithGoogle(idToken)
            } else {
                authViewModel.resetState()
            }
        } catch (e: Exception) {
            authViewModel.resetState()
        }
    }

    // GoogleSignInClient config
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(
            context.resources.getIdentifier(
                "default_web_client_id",
                "string",
                context.packageName
            )
        ))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

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
                val loginSuccess by authViewModel.loginSuccess.collectAsState()
                val errorMessage by authViewModel.errorMessage.collectAsState()
                LaunchedEffect(loginSuccess) {
                    if (loginSuccess) {
                        navController.navigate(AppRoutes.MAIN_SCREEN) {
                            popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                        }
                        authViewModel.resetState()
                    }
                }
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AppRoutes.MAIN_SCREEN) {
                            popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                        }
                        authViewModel.resetState()
                    },
                    onGoogleLogin = {
                        val signInIntent = googleSignInClient.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    },
                    onNavigateToSignup = {
                        navController.navigate(AppRoutes.SIGNUP_SCREEN) {
                            launchSingleTop = true
                        }
                    },
                    authViewModel = authViewModel
                )
                if (errorMessage != null) {
                    // Optionally show error dialog/snackbar
                }
            }
            composable(route = AppRoutes.SIGNUP_SCREEN) {
                val signupSuccess by authViewModel.loginSuccess.collectAsState()
                val errorMessage by authViewModel.errorMessage.collectAsState()
                LaunchedEffect(signupSuccess) {
                    if (signupSuccess) {
                        navController.navigate(AppRoutes.MAIN_SCREEN) {
                            popUpTo(AppRoutes.SIGNUP_SCREEN) { inclusive = true }
                        }
                        authViewModel.resetState()
                    }
                }
                SignupScreen(
                    onSignupSuccess = {
                        navController.navigate(AppRoutes.MAIN_SCREEN) {
                            popUpTo(AppRoutes.SIGNUP_SCREEN) { inclusive = true }
                        }
                        authViewModel.resetState()
                    },
                    onGoogleSignup = {
                        val signInIntent = googleSignInClient.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    },
                    onNavigateToLogin = {
                        navController.popBackStack(AppRoutes.LOGIN_SCREEN, inclusive = false)
                    },
                    authViewModel = authViewModel
                )
                if (errorMessage != null) {
                    // Optionally show error dialog/snackbar
                }
            }
            composable(route = AppRoutes.MAIN_SCREEN) {
                MainScreen(navController = navController)
            }
            composable(route = AppRoutes.ACTIVE_TRIP_SCREEN) {
                ActiveTripScreen(navController = navController)
            }
        }
    }
}
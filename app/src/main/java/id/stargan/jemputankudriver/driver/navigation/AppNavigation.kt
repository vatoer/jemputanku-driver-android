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
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AppRoutes.MAIN_SCREEN) {
                            popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                        }
                    },
                    onGoogleLogin = {
                        // TODO: Integrasi Google Sign-In
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
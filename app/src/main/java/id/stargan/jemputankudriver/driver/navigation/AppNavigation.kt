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
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

// Definisikan rute (alamat unik) untuk setiap layar
object AppRoutes {
    const val MAIN_SCREEN = "main"
    const val ACTIVE_TRIP_SCREEN = "active_trip"
    const val ONBOARDING_SCREEN = "onboarding"
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
        startDestination = if (isOnboardingShown) AppRoutes.MAIN_SCREEN else AppRoutes.ONBOARDING_SCREEN
    }

    if (startDestination != null) {
        NavHost(navController = navController, startDestination = startDestination!!) {
            composable(route = AppRoutes.ONBOARDING_SCREEN) {
                OnboardingScreen(
                    onFinish = {
                        scope.launch {
                            OnboardingPreferences.setOnboardingShown(context, true)
                            navController.navigate(AppRoutes.MAIN_SCREEN) {
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
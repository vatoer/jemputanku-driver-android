package id.stargan.jemputankudriver.driver.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.stargan.jemputankudriver.driver.ui.main.MainScreen
import id.stargan.jemputankudriver.feature.active_trip.ActiveTripScreen

// Definisikan rute (alamat unik) untuk setiap layar
object AppRoutes {
    const val MAIN_SCREEN = "main"
    const val ACTIVE_TRIP_SCREEN = "active_trip"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.MAIN_SCREEN) {
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
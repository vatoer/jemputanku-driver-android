package id.stargan.jemputankudriver.driver.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import id.stargan.jemputankudriver.R // Ganti dengan package R aplikasi Anda untuk icon
import id.stargan.jemputankudriver.feature.account.AccountScreen
import id.stargan.jemputankudriver.feature.home.HomeScreen
import id.stargan.jemputankudriver.feature.schedule.ScheduleScreen

// Enum untuk merepresentasikan setiap item navigasi
enum class MainNavigationItem(
    val title: String,
    val icon: Int // Resource ID untuk ikon
) {
    HOME(
        title = "Beranda",
        icon = R.drawable.ic_home // Ganti dengan ikon Anda
    ),
    SCHEDULE(
        title = "Jadwal",
        icon = R.drawable.ic_schedule // Ganti dengan ikon Anda
    ),
    ACCOUNT(
        title = "Akun",
        icon = R.drawable.ic_account // Ganti dengan ikon Anda
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Daftar item navigasi yang akan kita gunakan
    val navigationItems = MainNavigationItem.values().toList()

    // State untuk melacak item mana yang sedang aktif.
    // rememberSaveable memastikan state tidak hilang saat rotasi layar.
    var selectedItem by rememberSaveable {
        mutableStateOf(MainNavigationItem.HOME)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedItem.title) }, // Judul berubah sesuai tab
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            MainBottomNavigationBar(
                items = navigationItems,
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { innerPadding ->
        // Box ini akan berisi konten dari layar yang aktif
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                MainNavigationItem.HOME -> HomeScreen()
                MainNavigationItem.SCHEDULE -> ScheduleScreen()
                MainNavigationItem.ACCOUNT -> AccountScreen()
            }
        }
    }
}

@Composable
fun MainBottomNavigationBar(
    items: List<MainNavigationItem>,
    selectedItem: MainNavigationItem,
    onItemSelected: (MainNavigationItem) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onItemSelected(item) },
                label = { Text(item.title) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                alwaysShowLabel = true // Bisa diubah menjadi false jika hanya ingin ikon
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    // Anda perlu tema Compose untuk preview
    // Misalnya: YourAppTheme { MainScreen() }
    MainScreen()
}
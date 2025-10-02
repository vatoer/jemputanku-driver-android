package id.stargan.jemputankudriver.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Konten Halaman Beranda", style = MaterialTheme.typography.headlineMedium)
        // TODO: Implementasikan UI Beranda di sini
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Dengan adanya preview di sini, Anda bisa mengerjakan UI HomeScreen
    // secara terisolasi tanpa harus menjalankan seluruh aplikasi.
    HomeScreen()
}
package id.stargan.jemputankudriver.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import id.stargan.jemputankudriver.feature.home.components.ActiveTripCard
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onStartTripClick: () -> Unit,
    onLihatTripClick: () -> Unit = {},
    rute: String = "Terminal A - Terminal B",
    nextStop: String = "Terminal B"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ActiveTripCard tampil di atas, tombol di bawahnya
        ActiveTripCard(
            rute = rute,
            nextStop = nextStop,
            onLihatClick = onLihatTripClick
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onStartTripClick) {
            Text("Mulai Perjalanan")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onStartTripClick = {},
        onLihatTripClick = {},
        rute = "Terminal A - Terminal B",
        nextStop = "Terminal B"
    )
}
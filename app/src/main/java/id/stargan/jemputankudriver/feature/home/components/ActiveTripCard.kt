package id.stargan.jemputankudriver.feature.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActiveTripCard(
    rute: String,
    nextStop: String,
    onLihatClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Rute: $rute", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Next Stop: $nextStop", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onLihatClick) {
                    Text("Lihat")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveTripCardPreview() {
    ActiveTripCard(
        rute = "Terminal A - Terminal B",
        nextStop = "Terminal B",
        onLihatClick = {}
    )
}

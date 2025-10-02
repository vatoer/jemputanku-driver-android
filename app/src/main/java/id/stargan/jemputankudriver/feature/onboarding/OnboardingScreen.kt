package id.stargan.jemputankudriver.feature.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit = {},
    onReset: (() -> Unit)? = null // opsional, hanya untuk debug
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Selamat Datang di Jemputanku",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Aplikasi untuk driver bis jemputan di perusahaan, kementerian, sekolah, dan lainnya. Kelola perjalanan, jadwal, dan penumpang dengan mudah.",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 32.dp),
            lineHeight = 22.sp
        )
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mulai")
        }
        if (onReset != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onReset,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset Onboarding (Debug)")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onReset = {})
}

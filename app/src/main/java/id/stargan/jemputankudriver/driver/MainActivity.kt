package id.stargan.jemputankudriver.driver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import id.stargan.jemputankudriver.core.ui.theme.JemputankuDriverTheme
import id.stargan.jemputankudriver.driver.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JemputankuDriverTheme {
                AppNavigation()
            }
        }
    }
}

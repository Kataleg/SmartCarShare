package kz.smartcarshare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kz.smartcarshare.app.navigation.SmartCarShareApp
import kz.smartcarshare.app.ui.theme.SmartCarShareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCarShareTheme {
                SmartCarShareApp()
            }
        }
    }
}
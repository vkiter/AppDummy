package edu.khokhlovvaleriy.appdummy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edu.khokhlovvaleriy.appdummy.navegacion.AppNavigation
import edu.khokhlovvaleriy.appdummy.ui.theme.AppDummyTheme

// Única Activity de la app: solo aloja la navegación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppDummyTheme {
                AppNavigation()
            }
        }
    }
}
package de.julianschwers.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import de.julianschwers.diary.ui.theme.DiaryTheme

class MainActivity : ComponentActivity() {
    private lateinit var appState: AppState
    private val appEnvironment by lazy { AppEnvironment(context = applicationContext) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            appState = AppState(mainNavController = rememberNavController())
            
            DiaryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigationLayer(
                        appState = appState,
                        appEnvironment = appEnvironment,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
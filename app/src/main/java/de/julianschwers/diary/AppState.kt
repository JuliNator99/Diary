package de.julianschwers.diary

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController

class AppState(val mainNavController: NavHostController) {
    val snackBar = SnackbarHostState()
}
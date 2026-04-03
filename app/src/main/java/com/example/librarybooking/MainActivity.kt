package com.example.librarybooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.librarybooking.ui.auth.LoginScreen
import com.example.librarybooking.ui.auth.RegisterScreen
import com.example.librarybooking.ui.theme.LibraryBookingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryBookingAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onGoToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onGoToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}
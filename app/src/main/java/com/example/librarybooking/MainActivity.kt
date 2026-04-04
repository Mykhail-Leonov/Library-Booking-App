package com.example.librarybooking

import com.example.librarybooking.ui.booking.BookingScreen
import com.example.librarybooking.ui.booking.EditBookingScreen
import com.example.librarybooking.ui.home.HomeScreen
import com.example.librarybooking.ui.profile.ProfileScreen
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
import android.net.Uri

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
                    navController.navigate("home")
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

        composable("home") {
            HomeScreen(
                onOpenProfile = {
                    navController.navigate("profile")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onOpenBooking = { boothName ->
                    navController.navigate("booking/${Uri.encode(boothName)}")
                }
            )
        }

        composable("booking/{boothName}") { backStackEntry ->
            val boothName = backStackEntry.arguments?.getString("boothName") ?: ""
            BookingScreen(
                boothName = boothName,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBack = {
                    navController.popBackStack()
                },
                onEditBooking = {
                    navController.navigate("edit_booking")
                }
            )
        }

        composable("edit_booking") {
            EditBookingScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
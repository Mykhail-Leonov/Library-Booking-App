package com.example.librarybooking

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.librarybooking.ui.auth.AuthView
import com.example.librarybooking.ui.auth.LoginScreen
import com.example.librarybooking.ui.auth.RegisterScreen
import com.example.librarybooking.ui.booking.BookingScreen
import com.example.librarybooking.ui.booking.EditBookingScreen
import com.example.librarybooking.ui.home.HomeScreen
import com.example.librarybooking.ui.profile.ProfileScreen
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
    val authView: AuthView = viewModel()
    val authState by authView.authState.collectAsState()

    val startDestination = if (authView.isLoggedIn()) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                authState = authState,
                onGoToRegister = {
                    authView.resetState()
                    navController.navigate("register")
                },
                onLoginClick = { email, password ->
                    authView.login(email, password)
                },
                onLoginSuccess = {
                    authView.resetState()
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                authState = authState,
                onGoToLogin = {
                    authView.resetState()
                    navController.popBackStack()
                },
                onRegisterClick = { fullName, studentId, email, password ->
                    authView.register(fullName, studentId, email, password)
                },
                onRegisterSuccess = {
                    authView.resetState()
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
                    authView.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
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
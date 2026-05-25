package com.example.maxlish.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.maxlish.data.repository.FirebaseAuthRepository
import com.example.maxlish.ui.screen.home.HomeScreen
import com.example.maxlish.ui.screen.login.LoginScreen
import com.example.maxlish.ui.screen.profile.ProfileScreen
import com.example.maxlish.ui.screen.register.RegisterScreen

object AppDestinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
    const val HOME = "home"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val authRepository = FirebaseAuthRepository()
    // Xác định màn hình bắt đầu: nếu đã đăng nhập → home, chưa → login
    val startDestination = if (authRepository.getCurrentUser() != null) {
        AppDestinations.HOME
    } else {
        AppDestinations.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppDestinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppDestinations.REGISTER)
                }
            )
        }

        composable(AppDestinations.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(AppDestinations.PROFILE) {
                        popUpTo(AppDestinations.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppDestinations.PROFILE) {
            ProfileScreen(
                onSaveSuccess = {
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.LOGIN) { inclusive = true }
                    }
                },
                onLogout = {
                    navController.navigate(AppDestinations.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.HOME) {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(AppDestinations.PROFILE)
                },
                onLogout = {
                    navController.navigate(AppDestinations.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

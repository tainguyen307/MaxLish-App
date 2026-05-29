package com.example.maxlish.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.maxlish.data.repository.FirebaseAuthRepository
import com.example.maxlish.data.repository.FirebaseProgressRepository
import com.example.maxlish.ui.screen.home.HomeRoute
import com.example.maxlish.ui.screen.login.LoginScreen
import com.example.maxlish.ui.screen.profile.ProfileRoute
import com.example.maxlish.ui.screen.progress.ProgressScreen
import com.example.maxlish.ui.screen.progress.ProgressViewModel
import com.example.maxlish.ui.screen.register.RegisterScreen

object AppDestinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
    const val HOME = "home"
    const val PROGRESS = "progress"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val authRepository = FirebaseAuthRepository()

    val startDestination =
        if (authRepository.getCurrentUser() != null) {
            AppDestinations.HOME
        } else {
            AppDestinations.LOGIN
        }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar =
        currentRoute == AppDestinations.HOME ||
                currentRoute == AppDestinations.PROFILE ||
                currentRoute == AppDestinations.PROGRESS

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == AppDestinations.HOME,
                        onClick = {
                            navController.navigate(AppDestinations.HOME) {
                                launchSingleTop = true
                                popUpTo(AppDestinations.HOME) { inclusive = false }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text("Home")
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppDestinations.PROGRESS,
                        onClick = {
                            navController.navigate(AppDestinations.PROGRESS) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Progress"
                            )
                        },
                        label = {
                            Text("Progress")
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppDestinations.PROFILE,
                        onClick = {
                            navController.navigate(AppDestinations.PROFILE) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text("Profile")
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppDestinations.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AppDestinations.HOME) {
                            popUpTo(AppDestinations.LOGIN) {
                                inclusive = true
                            }
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
                            popUpTo(AppDestinations.REGISTER) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(AppDestinations.PROFILE) {
                ProfileRoute(
                    onSaveSuccess = {
                        navController.navigate(AppDestinations.HOME) {
                            popUpTo(AppDestinations.LOGIN) {
                                inclusive = true
                            }
                        }
                    },
                    onLogout = {
                        navController.navigate(AppDestinations.LOGIN) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(AppDestinations.HOME) {
                HomeRoute(
                    onNavigateToProfile = {
                        navController.navigate(AppDestinations.PROFILE)
                    },
                    onNavigateToProgress = {
                        navController.navigate(AppDestinations.PROGRESS)
                    }
                )
            }

            composable(AppDestinations.PROGRESS) {
                val progressRepository = FirebaseProgressRepository()
                val viewModel: ProgressViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return ProgressViewModel(authRepository, progressRepository) as T
                        }
                    }
                )
                ProgressScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
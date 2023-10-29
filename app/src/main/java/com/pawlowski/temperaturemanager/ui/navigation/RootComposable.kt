package com.pawlowski.temperaturemanager.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawlowski.temperaturemanager.ui.screens.login.LoginScreen
import com.pawlowski.temperaturemanager.ui.screens.login.LoginViewModel
import com.pawlowski.temperaturemanager.ui.screens.splash.SplashScreen
import com.pawlowski.temperaturemanager.ui.screens.splash.SplashViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun RootComposable() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.name) {
        composable(route = Screen.Login.name) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                state = loginViewModel.stateFlow.collectAsState().value,
                onEvent = loginViewModel::onNewEvent,
            )
            loginViewModel.navigationFlow.observeNavigation(navController = navController)
        }
        composable(route = Screen.Home.name) {
            Text(text = "Home")
        }
        composable(route = Screen.Splash.name) {
            val splashViewModel = hiltViewModel<SplashViewModel>()
            splashViewModel.stateFlow.collectAsState()
            SplashScreen()
            splashViewModel.navigationFlow.observeNavigation(navController = navController)
        }
    }
}

@Composable
private fun Flow<Direction>.observeNavigation(navController: NavController) {
    LaunchedEffect(Unit) {
        collect { direction ->
            when (direction) {
                is Back -> {
                    navController.popBackStack()
                }

                else -> {
                    navController.navigate(route = direction.destination.name) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

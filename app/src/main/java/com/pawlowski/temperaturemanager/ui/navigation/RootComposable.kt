package com.pawlowski.temperaturemanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawlowski.temperaturemanager.ui.screens.home.HomeScreen
import com.pawlowski.temperaturemanager.ui.screens.home.HomeViewModel
import com.pawlowski.temperaturemanager.ui.screens.login.LoginScreen
import com.pawlowski.temperaturemanager.ui.screens.login.LoginViewModel
import com.pawlowski.temperaturemanager.ui.screens.searchDevices.SearchDevicesScreen
import com.pawlowski.temperaturemanager.ui.screens.searchDevices.SearchDevicesViewModel
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
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                state = homeViewModel.stateFlow.collectAsState().value,
                onEvent = homeViewModel::onNewEvent,
            )
            homeViewModel.navigationFlow.observeNavigation(navController = navController)
        }
        composable(route = Screen.Splash.name) {
            val splashViewModel = hiltViewModel<SplashViewModel>()
            splashViewModel.stateFlow.collectAsState()
            SplashScreen()
            splashViewModel.navigationFlow.observeNavigation(navController = navController)
        }
        composable(route = Screen.SearchDevices.name) {
            val searchViewModel = hiltViewModel<SearchDevicesViewModel>()
            val state by searchViewModel.stateFlow.collectAsState()
            SearchDevicesScreen(
                state = state,
                onEvent = {
                    searchViewModel.onNewEvent(it)
                },
            )
            searchViewModel.navigationFlow.observeNavigation(navController = navController)
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

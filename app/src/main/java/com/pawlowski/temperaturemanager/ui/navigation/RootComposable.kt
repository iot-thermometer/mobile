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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.pawlowski.temperaturemanager.ui.screens.bluetoothOff.BluetoothOffScreen
import com.pawlowski.temperaturemanager.ui.screens.home.HomeScreen
import com.pawlowski.temperaturemanager.ui.screens.home.HomeViewModel
import com.pawlowski.temperaturemanager.ui.screens.login.LoginScreen
import com.pawlowski.temperaturemanager.ui.screens.login.LoginViewModel
import com.pawlowski.temperaturemanager.ui.screens.noBluetoothPermission.NoBluetoothPermissionScreen
import com.pawlowski.temperaturemanager.ui.screens.searchDevices.SearchDevicesScreen
import com.pawlowski.temperaturemanager.ui.screens.searchDevices.SearchDevicesViewModel
import com.pawlowski.temperaturemanager.ui.screens.splash.SplashScreen
import com.pawlowski.temperaturemanager.ui.screens.splash.SplashViewModel
import com.pawlowski.temperaturemanager.ui.screens.wifiInfo.WifiInfoScreen
import com.pawlowski.temperaturemanager.ui.screens.wifiInfo.WifiInfoViewModel
import com.pawlowski.temperaturemanager.ui.utils.rememberBluetoothMultiplePermissionsState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RootComposable(
    isBluetoothEnabled: Boolean,
) {
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

            ContentOrBluetoothInfo(isBluetoothEnabled = isBluetoothEnabled) {
                val state by searchViewModel.stateFlow.collectAsState()
                SearchDevicesScreen(
                    state = state,
                    onEvent = {
                        searchViewModel.onNewEvent(it)
                    },
                )
            }

            searchViewModel.navigationFlow.observeNavigation(navController = navController)
        }
        composable(route = Screen.WifiInfo.name) {
            val wifiInfoViewModel = hiltViewModel<WifiInfoViewModel>()

            ContentOrBluetoothInfo(isBluetoothEnabled = isBluetoothEnabled) {
                val state by wifiInfoViewModel.stateFlow.collectAsState()
                WifiInfoScreen(
                    state = state,
                    onEvent = {
                        wifiInfoViewModel.onNewEvent(it)
                    },
                )
            }

            wifiInfoViewModel.navigationFlow.observeNavigation(navController = navController)
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
                        direction.popUpTo?.let {
                            popUpTo(route = it.name) {
                                inclusive = direction.popUpToInclusive
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContentOrBluetoothInfo(
    isBluetoothEnabled: Boolean,
    content: @Composable () -> Unit,
) {
    val permissionsGranted =
        rememberBluetoothMultiplePermissionsState().allPermissionsGranted

    if (permissionsGranted) {
        if (isBluetoothEnabled) {
            content()
        } else {
            BluetoothOffScreen()
        }
    } else {
        NoBluetoothPermissionScreen()
    }
}

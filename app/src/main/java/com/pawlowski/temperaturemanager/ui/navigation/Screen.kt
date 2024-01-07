package com.pawlowski.temperaturemanager.ui.navigation

sealed interface Screen {
    val name: String
    val directions: List<Direction>

    object Login : Screen {
        override val name: String = "Login"
        override val directions: List<Direction> = LoginDirection.values().toList()

        enum class LoginDirection(
            override val destination: Screen,
            override val popUpTo: Screen? = null,
            override val popUpToInclusive: Boolean = false,
        ) : Direction {
            HOME(
                destination = Home,
                popUpTo = Login,
                popUpToInclusive = true,
            ),
        }
    }

    object Home : Screen {
        override val name: String = "Home"
        override val directions: List<Direction> = HomeDirection.values().toList()

        enum class HomeDirection(
            override val destination: Screen,
            override val popUpTo: Screen? = null,
            override val popUpToInclusive: Boolean = false,
        ) : Direction {
            SEARCH_DEVICES(destination = SearchDevices),
            READINGS(destination = Readings),
            LOGIN(
                destination = Login,
                popUpTo = Home,
                popUpToInclusive = true,
            ),
        }
    }

    object SearchDevices : Screen {
        override val name: String = "SearchDevices"
        override val directions: List<Direction> = SearchDevicesDirection.values().toList()

        enum class SearchDevicesDirection(override val destination: Screen) : Direction {
            WIFI_INFO(WifiInfo),
        }
    }

    object WifiInfo : Screen {
        override val name: String = "WifiInfo"
        override val directions: List<Direction> = WifiInfoDirection.values().toList()

        enum class WifiInfoDirection(
            override val destination: Screen,
            override val popUpTo: Screen? = null,
            override val popUpToInclusive: Boolean = false,
        ) : Direction {
            HOME(
                destination = Home,
                popUpTo = Home,
                popUpToInclusive = false,
            ),
        }
    }

    object Readings : Screen {
        override val name: String = "Readings"
        override val directions: List<Direction> = ReadingsDirection.values().toList()

        enum class ReadingsDirection(
            override val destination: Screen,
            override val popUpTo: Screen? = null,
            override val popUpToInclusive: Boolean = false,
        ) : Direction {
            DEVICE_SETTINGS(destination = DeviceSettings),
        }
    }

    object DeviceSettings : Screen {
        override val name: String = "DeviceSettings"
        override val directions: List<Direction> = DeviceSettingsDirection.values().toList()

        enum class DeviceSettingsDirection(
            override val destination: Screen,
            override val popUpTo: Screen? = null,
            override val popUpToInclusive: Boolean = false,
        ) : Direction {
            HOME(
                destination = Home,
                popUpTo = Home,
                popUpToInclusive = true,
            ),
        }
    }

    object Alerts : Screen {
        override val name: String = "Alerts"
        override val directions: List<Direction> = AlertsDirection.values().toList()

        enum class AlertsDirection(
            override val destination: Screen,
            override val popUpTo: Screen? = null,
            override val popUpToInclusive: Boolean = false,
        ) : Direction
    }

    object Splash : Screen {
        override val name: String = "Splash"
        override val directions: List<Direction> = SplashDirection.values().toList()

        enum class SplashDirection(
            override val destination: Screen,
        ) : Direction {
            LOGIN(Login),
            HOME(Home),
            ;

            override val popUpTo: Screen? = Splash
            override val popUpToInclusive: Boolean = true
        }
    }

    // Needed for navigating back
    object Back : Screen {
        override val name: String = "Back"
        override val directions: List<Direction> = emptyList()
    }
}

interface Direction {
    val destination: Screen
    val popUpTo: Screen?
        get() = null
    val popUpToInclusive: Boolean
        get() = false
}

object Back : Direction {
    override val destination: Screen = Screen.Back
}

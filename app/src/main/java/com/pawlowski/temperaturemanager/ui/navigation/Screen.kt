package com.pawlowski.temperaturemanager.ui.navigation

sealed interface Screen {
    val name: String
    val directions: List<Direction>

    object Login : Screen {
        override val name: String = "Login"
        override val directions: List<Direction> = LoginDirection.values().toList()

        enum class LoginDirection(override val destination: Screen) : Direction {
            HOME(destination = Home),
        }
    }

    object Home : Screen {
        override val name: String = "Home"
        override val directions: List<Direction> = HomeDirection.values().toList()

        enum class HomeDirection(override val destination: Screen) : Direction
    }

    object Splash : Screen {
        override val name: String = "Splash"
        override val directions: List<Direction> = SplashDirection.values().toList()

        enum class SplashDirection(override val destination: Screen) : Direction {
            LOGIN(Login),
            HOME(Home),
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
}

object Back : Direction {
    override val destination: Screen = Screen.Back
}

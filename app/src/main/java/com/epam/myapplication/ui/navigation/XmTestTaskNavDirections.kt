package com.epam.myapplication.ui.navigation

sealed class XmTestTaskNavDirections(val route: String) {
    data object Initial : XmTestTaskNavDirections("initial_screen")
    data object Survey : XmTestTaskNavDirections("survey_screen")
}
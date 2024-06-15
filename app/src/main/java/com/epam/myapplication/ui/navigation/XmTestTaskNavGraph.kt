package com.epam.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.epam.myapplication.ui.screens.initial.InitialScreen
import com.epam.myapplication.ui.screens.survey.SurveyScreen


@Composable
fun XMTestTaskNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = XmTestTaskNavDirections.Initial.route) {
        composable(XmTestTaskNavDirections.Initial.route) { InitialScreen(navController) }
        composable(XmTestTaskNavDirections.Survey.route) { SurveyScreen(navController) }
    }
}
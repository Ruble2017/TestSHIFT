package com.tomsk.testshift.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.tomsk.testshift.screens.MainDestination
import com.tomsk.testshift.screens.MainScreen
import com.tomsk.testshift.screens.PersonDetailsDestination
import com.tomsk.testshift.screens.PersonDetailsScreen

@Composable
fun AppNavGraph()
{
    val navHostController = rememberNavController()

    NavHost(navHostController,
        //startDestination = Screen.MainScreen.route
        startDestination = MainDestination.route
        ){

        composable(MainDestination.route,
            enterTransition = { slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)) },
            exitTransition = { slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)) },
            popEnterTransition = { slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            ) },
            popExitTransition = { slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            ) },

        ){
            MainScreen(
                navigateToDetailScreen =  {
                    navHostController.navigate("${PersonDetailsDestination.route}/${it}")
                }
            )
        }

        composable(route = PersonDetailsDestination.routeWithArgs,
                    arguments = listOf(navArgument(PersonDetailsDestination.itemIdArg){
                              type = NavType.IntType
                    }),
            enterTransition = { slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)) },
            exitTransition = { slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)) },
            popEnterTransition = { slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            ) },
            popExitTransition = { slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700) ) }

        ){
            PersonDetailsScreen(navHostController)
        }

    }
}
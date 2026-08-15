package ir.mehdi.imposter.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.mehdi.imposter.presentation.screen.about.AboutScreen
import ir.mehdi.imposter.presentation.screen.discussion.DiscussionStartScreen
import ir.mehdi.imposter.presentation.screen.finished.GameFinishedScreen
import ir.mehdi.imposter.presentation.screen.home.HomeScreen
import ir.mehdi.imposter.presentation.screen.player.PlayerScreen
import ir.mehdi.imposter.presentation.screen.rules.RulesScreen
import ir.mehdi.imposter.presentation.screen.settings.SettingsScreen
import ir.mehdi.imposter.presentation.screen.setup.GameSetupScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartGame = {
                    navController.navigate(Screen.GameSetup.route)
                },
                onRules = {
                    navController.navigate(Screen.Rules.route)
                },
                onAbout = {
                    navController.navigate(Screen.About.route)
                }
            )
        }

        composable(Screen.GameSetup.route) {
            GameSetupScreen(
                onBack = { navController.popBackStack() },
                onGameStarted = {
                    navController.navigate(Screen.PlayerCard.route) {
                        popUpTo(Screen.GameSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.PlayerCard.route) {
            PlayerScreen(
                onGameFinished = {
                    navController.navigate(Screen.DiscussionStart.route) {
                        // Remove the card screen from the back stack so the
                        // system back button returns Home instead of trapping
                        // the user between the last card and "discussion start".
                        popUpTo(Screen.PlayerCard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.DiscussionStart.route) {
            DiscussionStartScreen(
                onStartDiscussion = {
                    navController.navigate(Screen.GameFinished.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.GameFinished.route) {
            GameFinishedScreen(
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Rules.route) {
            RulesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

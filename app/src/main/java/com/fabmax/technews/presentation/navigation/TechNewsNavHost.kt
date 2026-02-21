package com.fabmax.technews.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fabmax.technews.presentation.ui.article.ArticleScreen
import com.fabmax.technews.presentation.ui.bookmarks.BookmarksScreen
import com.fabmax.technews.presentation.ui.feed.FeedScreen
import com.fabmax.technews.presentation.ui.home.HomeScreen
import java.net.URLDecoder

@Composable
fun TechNewsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onArticleClick = { article ->
                    navController.navigate(Screen.Article.createRoute(article.id))
                }
            )
        }

        composable(Screen.Bookmarks.route) {
            BookmarksScreen(
                onArticleClick = { article ->
                    navController.navigate(Screen.Article.createRoute(article.id))
                }
            )
        }

        composable(Screen.Feed.route) {
            FeedScreen()
        }

        composable(
            route = Screen.Article.route,
            arguments = listOf(
                navArgument(NavArgs.ARTICLE_ID) {
                    type = NavType.StringType
                }
            ),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
        ) {
            ArticleScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

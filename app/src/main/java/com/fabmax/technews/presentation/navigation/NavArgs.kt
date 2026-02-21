package com.fabmax.technews.presentation.navigation

object NavArgs {
    const val ARTICLE_ID = "articleId"
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Bookmarks : Screen("bookmarks")
    data object Feed : Screen("feed")
    data object Article : Screen("article/{${NavArgs.ARTICLE_ID}}") {
        fun createRoute(articleId: String) = "article/${Uri.encode(articleId)}"
    }
}

private object Uri {
    fun encode(value: String): String = java.net.URLEncoder.encode(value, "UTF-8")
}

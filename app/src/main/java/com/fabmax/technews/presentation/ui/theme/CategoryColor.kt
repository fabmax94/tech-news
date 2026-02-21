package com.fabmax.technews.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.fabmax.technews.domain.model.ArticleCategory

@Composable
fun categoryColor(category: ArticleCategory): Color {
    return when (category) {
        ArticleCategory.GENERAL -> ColorGeneral
        ArticleCategory.DEV -> ColorDev
        ArticleCategory.AI -> ColorAI
        ArticleCategory.SECURITY -> ColorSecurity
        ArticleCategory.MOBILE -> ColorMobile
        ArticleCategory.CLOUD -> ColorCloud
        ArticleCategory.OPEN_SOURCE -> ColorOpenSource
        ArticleCategory.STARTUPS -> ColorStartups
    }
}

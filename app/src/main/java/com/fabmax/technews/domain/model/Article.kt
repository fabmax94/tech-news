package com.fabmax.technews.domain.model

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val link: String,
    val imageUrl: String?,
    val author: String?,
    val publishedAt: Long,
    val sourceId: String,
    val sourceName: String,
    val sourceLogoUrl: String?,
    val isBookmarked: Boolean = false,
    val isRead: Boolean = false,
    val category: ArticleCategory = ArticleCategory.GENERAL
)

enum class ArticleCategory(val label: String) {
    GENERAL("Geral"),
    DEV("Desenvolvimento"),
    AI("Inteligência Artificial"),
    SECURITY("Segurança"),
    MOBILE("Mobile"),
    CLOUD("Cloud"),
    OPEN_SOURCE("Open Source"),
    STARTUPS("Startups")
}

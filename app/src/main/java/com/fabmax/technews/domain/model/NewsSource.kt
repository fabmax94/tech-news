package com.fabmax.technews.domain.model

data class NewsSource(
    val id: String,
    val name: String,
    val feedUrl: String,
    val siteUrl: String,
    val logoUrl: String?,
    val category: ArticleCategory,
    val isEnabled: Boolean = true
)

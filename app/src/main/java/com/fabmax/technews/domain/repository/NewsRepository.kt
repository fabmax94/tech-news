package com.fabmax.technews.domain.repository

import com.fabmax.technews.domain.model.Article
import com.fabmax.technews.domain.model.ArticleCategory
import com.fabmax.technews.domain.model.NewsSource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getArticles(category: ArticleCategory? = null): Flow<List<Article>>
    fun getBookmarkedArticles(): Flow<List<Article>>
    fun getArticleById(id: String): Flow<Article?>
    suspend fun refreshArticles()
    suspend fun toggleBookmark(articleId: String)
    suspend fun markAsRead(articleId: String)
    fun getSources(): Flow<List<NewsSource>>
    suspend fun toggleSource(sourceId: String)
    suspend fun searchArticles(query: String): List<Article>
}

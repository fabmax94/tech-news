package com.fabmax.technews.data.repository

import android.util.Log
import com.fabmax.technews.data.local.dao.ArticleDao
import com.fabmax.technews.data.local.dao.SourceDao
import com.fabmax.technews.data.local.entity.ArticleEntity
import com.fabmax.technews.data.local.entity.SourceEntity
import com.fabmax.technews.data.model.DefaultSources
import com.fabmax.technews.data.remote.RssFeedService
import com.fabmax.technews.data.remote.RssParser
import com.fabmax.technews.domain.model.Article
import com.fabmax.technews.domain.model.ArticleCategory
import com.fabmax.technews.domain.model.NewsSource
import com.fabmax.technews.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao,
    private val sourceDao: SourceDao,
    private val rssFeedService: RssFeedService,
    private val rssParser: RssParser
) : NewsRepository {

    override fun getArticles(category: ArticleCategory?): Flow<List<Article>> {
        return if (category == null) {
            articleDao.getAllArticles().map { entities -> entities.map { it.toDomain() } }
        } else {
            articleDao.getArticlesByCategory(category.name).map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }

    override fun getBookmarkedArticles(): Flow<List<Article>> {
        return articleDao.getBookmarkedArticles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getArticleById(id: String): Flow<Article?> {
        return articleDao.getArticleById(id).map { it?.toDomain() }
    }

    override suspend fun refreshArticles() {
        withContext(Dispatchers.IO) {
            ensureSourcesPopulated()

            val enabledSources = sourceDao.getEnabledSources()
            val threshold = System.currentTimeMillis() - CACHE_RETENTION_MS
            articleDao.deleteOldArticles(threshold)

            coroutineScope {
                enabledSources.map { source ->
                    async {
                        fetchArticlesFromSource(source)
                    }
                }.awaitAll()
            }
        }
    }

    private suspend fun fetchArticlesFromSource(source: SourceEntity) {
        try {
            val xmlContent = rssFeedService.fetchFeed(source.feedUrl)
            val articles = rssParser.parseRss(xmlContent, source)
            if (articles.isNotEmpty()) {
                articleDao.insertArticles(articles)
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Failed to fetch ${source.name}: ${e.message}")
        }
    }

    override suspend fun toggleBookmark(articleId: String) {
        articleDao.toggleBookmark(articleId)
    }

    override suspend fun markAsRead(articleId: String) {
        articleDao.markAsRead(articleId)
    }

    override fun getSources(): Flow<List<NewsSource>> = flow {
        if (sourceDao.count() == 0) {
            sourceDao.insertSources(DefaultSources.all)
        }
        emitAll(sourceDao.getAllSources().map { entities -> entities.map { it.toDomain() } })
    }

    override suspend fun toggleSource(sourceId: String) {
        sourceDao.toggleSource(sourceId)
    }

    override suspend fun searchArticles(query: String): List<Article> {
        return articleDao.searchArticles(query).map { it.toDomain() }
    }

    private suspend fun ensureSourcesPopulated() {
        val count = sourceDao.count()
        if (count == 0) {
            sourceDao.insertSources(DefaultSources.all)
        }
    }

    companion object {
        private const val CACHE_RETENTION_MS = 7L * 24 * 60 * 60 * 1000 // 7 days
    }
}

private fun ArticleEntity.toDomain() = Article(
    id = id,
    title = title,
    description = description,
    content = content,
    link = link,
    imageUrl = imageUrl,
    author = author,
    publishedAt = publishedAt,
    sourceId = sourceId,
    sourceName = sourceName,
    sourceLogoUrl = sourceLogoUrl,
    isBookmarked = isBookmarked,
    isRead = isRead,
    category = try { ArticleCategory.valueOf(category) } catch (e: Exception) { ArticleCategory.GENERAL }
)

private fun SourceEntity.toDomain() = NewsSource(
    id = id,
    name = name,
    feedUrl = feedUrl,
    siteUrl = siteUrl,
    logoUrl = logoUrl,
    category = try { ArticleCategory.valueOf(category) } catch (e: Exception) { ArticleCategory.GENERAL },
    isEnabled = isEnabled
)

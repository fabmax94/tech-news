package com.fabmax.technews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fabmax.technews.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE isBookmarked = 1 ORDER BY publishedAt DESC")
    fun getBookmarkedArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: String): Flow<ArticleEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("UPDATE articles SET isBookmarked = NOT isBookmarked WHERE id = :id")
    suspend fun toggleBookmark(id: String)

    @Query("UPDATE articles SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query(
        """
        SELECT * FROM articles
        WHERE title LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
        ORDER BY publishedAt DESC
        LIMIT 50
        """
    )
    suspend fun searchArticles(query: String): List<ArticleEntity>

    @Query("DELETE FROM articles WHERE isBookmarked = 0 AND fetchedAt < :threshold")
    suspend fun deleteOldArticles(threshold: Long)

    @Query("SELECT COUNT(*) FROM articles WHERE sourceId = :sourceId")
    suspend fun countBySource(sourceId: String): Int
}

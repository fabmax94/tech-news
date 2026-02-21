package com.fabmax.technews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
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
    val category: String = "GENERAL",
    val fetchedAt: Long = System.currentTimeMillis()
)

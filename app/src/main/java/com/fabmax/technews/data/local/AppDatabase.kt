package com.fabmax.technews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fabmax.technews.data.local.dao.ArticleDao
import com.fabmax.technews.data.local.dao.SourceDao
import com.fabmax.technews.data.local.entity.ArticleEntity
import com.fabmax.technews.data.local.entity.SourceEntity

@Database(
    entities = [ArticleEntity::class, SourceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun sourceDao(): SourceDao
}

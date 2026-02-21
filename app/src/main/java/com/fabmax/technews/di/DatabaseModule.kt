package com.fabmax.technews.di

import android.content.Context
import androidx.room.Room
import com.fabmax.technews.data.local.AppDatabase
import com.fabmax.technews.data.local.dao.ArticleDao
import com.fabmax.technews.data.local.dao.SourceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tech_news.db"
        ).build()
    }

    @Provides
    fun provideArticleDao(database: AppDatabase): ArticleDao = database.articleDao()

    @Provides
    fun provideSourceDao(database: AppDatabase): SourceDao = database.sourceDao()
}

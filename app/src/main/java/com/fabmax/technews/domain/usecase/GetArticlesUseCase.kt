package com.fabmax.technews.domain.usecase

import com.fabmax.technews.domain.model.Article
import com.fabmax.technews.domain.model.ArticleCategory
import com.fabmax.technews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(category: ArticleCategory? = null): Flow<List<Article>> {
        return repository.getArticles(category)
    }
}

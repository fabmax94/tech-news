package com.fabmax.technews.domain.usecase

import com.fabmax.technews.domain.model.Article
import com.fabmax.technews.domain.repository.NewsRepository
import javax.inject.Inject

class SearchArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String): List<Article> {
        return repository.searchArticles(query)
    }
}

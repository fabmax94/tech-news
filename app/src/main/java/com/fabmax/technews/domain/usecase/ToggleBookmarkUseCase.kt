package com.fabmax.technews.domain.usecase

import com.fabmax.technews.domain.repository.NewsRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(articleId: String) {
        repository.toggleBookmark(articleId)
    }
}

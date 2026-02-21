package com.fabmax.technews.presentation.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabmax.technews.domain.model.Article
import com.fabmax.technews.domain.repository.NewsRepository
import com.fabmax.technews.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    val bookmarks: StateFlow<List<Article>> = newsRepository
        .getBookmarkedArticles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleBookmark(articleId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(articleId)
        }
    }
}

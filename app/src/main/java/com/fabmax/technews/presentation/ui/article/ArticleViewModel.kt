package com.fabmax.technews.presentation.ui.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabmax.technews.domain.model.Article
import com.fabmax.technews.domain.repository.NewsRepository
import com.fabmax.technews.domain.usecase.ToggleBookmarkUseCase
import com.fabmax.technews.presentation.navigation.NavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ArticleUiState(
    val article: Article? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val newsRepository: NewsRepository,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val articleId: String = checkNotNull(savedStateHandle[NavArgs.ARTICLE_ID])

    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    init {
        loadArticle()
    }

    private fun loadArticle() {
        viewModelScope.launch {
            newsRepository.getArticleById(articleId).collect { article ->
                _uiState.value = ArticleUiState(article = article, isLoading = false)
            }
        }
        viewModelScope.launch {
            newsRepository.markAsRead(articleId)
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            toggleBookmarkUseCase(articleId)
        }
    }
}

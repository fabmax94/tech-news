package com.fabmax.technews.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabmax.technews.domain.model.Article
import com.fabmax.technews.domain.model.ArticleCategory
import com.fabmax.technews.domain.usecase.GetArticlesUseCase
import com.fabmax.technews.domain.usecase.RefreshArticlesUseCase
import com.fabmax.technews.domain.usecase.SearchArticlesUseCase
import com.fabmax.technews.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val selectedCategory: ArticleCategory? = null,
    val searchQuery: String = "",
    val searchResults: List<Article> = emptyList(),
    val isSearching: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val refreshArticlesUseCase: RefreshArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val searchArticlesUseCase: SearchArticlesUseCase
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<ArticleCategory?>(null)
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        observeArticles()
        observeSearch()
        refreshIfNeeded()
    }

    private fun observeArticles() {
        viewModelScope.launch {
            _selectedCategory.flatMapLatest { category ->
                getArticlesUseCase(category)
            }.collect { articles ->
                _uiState.value = _uiState.value.copy(
                    articles = articles,
                    isLoading = false
                )
            }
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.value = _uiState.value.copy(
                            searchResults = emptyList(),
                            isSearching = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(isSearching = true)
                        val results = searchArticlesUseCase(query)
                        _uiState.value = _uiState.value.copy(
                            searchResults = results,
                            isSearching = false
                        )
                    }
                }
        }
    }

    private fun refreshIfNeeded() {
        viewModelScope.launch {
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            try {
                refreshArticlesUseCase()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao atualizar notícias"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }

    fun selectCategory(category: ArticleCategory?) {
        _selectedCategory.value = category
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun toggleBookmark(articleId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(articleId)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

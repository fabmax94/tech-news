package com.fabmax.technews.presentation.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabmax.technews.domain.model.NewsSource
import com.fabmax.technews.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val sources: StateFlow<List<NewsSource>> = newsRepository
        .getSources()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleSource(sourceId: String) {
        viewModelScope.launch {
            newsRepository.toggleSource(sourceId)
        }
    }
}

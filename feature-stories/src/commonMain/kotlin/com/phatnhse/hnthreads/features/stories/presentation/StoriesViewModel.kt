package com.phatnhse.hnthreads.features.stories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phatnhse.hnthreads.features.stories.data.repository.StoriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StoriesViewModel(
    private val repository: StoriesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<StoriesUiState>(StoriesUiState.Loading)
    val uiState: StateFlow<StoriesUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            repository.getStoriesFlow()
                .catch { exception ->
                    _uiState.value = StoriesUiState.Error(exception.message ?: "Unknown error")
                }
                .collect { stories ->
                    _uiState.value = if (stories.isEmpty()) {
                        StoriesUiState.Loading
                    } else {
                        StoriesUiState.Success(stories)
                    }
                }
        }

        refresh()
    }
    
    fun refresh() {
        viewModelScope.launch {
            try {
                repository.refreshStories() // Updates database, UI reacts automatically
            } catch (e: Exception) {
                _uiState.value = StoriesUiState.Error(e.message ?: "Failed to refresh stories")
            }
        }
    }
    
    fun getStoryFlow(id: Long) = repository.getStoryFlow(id)
}
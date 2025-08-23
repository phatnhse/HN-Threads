package com.phatnhse.hnthreads.features.stories.presentation

import androidx.compose.runtime.Immutable
import com.phatnhse.hnthreads.shared.data.models.Story

@Immutable
sealed interface StoriesUiState {
    data object Loading : StoriesUiState
    data class Success(val stories: List<Story>) : StoriesUiState
    data class Error(val message: String) : StoriesUiState
}
package com.phatnhse.hn.threads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phatnhse.hn.threads.database.entity.HackerNewItem
import com.phatnhse.hn.threads.repo.HackerNewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val repositoryImpl: HackerNewsRepository,
) : ViewModel() {

    val feedState: StateFlow<List<HackerNewItem>> =
        repositoryImpl.getTopStories()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    private val _showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showProgressBar = _showProgressBar.asStateFlow()

    init {
        viewModelScope.launch {
            _showProgressBar.value = true
            repositoryImpl.syncFeed()
            _showProgressBar.value = false
        }
    }
}
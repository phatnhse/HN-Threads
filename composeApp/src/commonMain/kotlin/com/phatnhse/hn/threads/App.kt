package com.phatnhse.hn.threads

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phatnhse.hn.news.di.networkModule
import com.phatnhse.hn.threads.database.di.databaseModule
import com.phatnhse.hn.threads.di.commonModules
import com.phatnhse.hn.threads.di.platformModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(commonModules, networkModule, platformModule, databaseModule)
    }) {
        AppContent()
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun AppContent(
    viewModel: AppViewModel = koinViewModel<AppViewModel>()
) {
    val items = viewModel.feedState.collectAsState().value

    MaterialTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items, key = { it.id }) {
                Column(
                    modifier = Modifier.padding(all = 16.dp)
                ) {
                    Text(text = it.title, style = MaterialTheme.typography.subtitle1)
                    Text(text = it.by, style = MaterialTheme.typography.subtitle2)
                }
            }
        }

        if (viewModel.showProgressBar.collectAsState().value) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.surface,
            )
        }
    }
}
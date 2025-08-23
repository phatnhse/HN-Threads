package com.phatnhse.hnthreads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.phatnhse.hnthreads.navigation.HNNavigation
import com.phatnhse.hnthreads.ui.theme.HNTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HNTheme {
        HNNavigation()
    }
}
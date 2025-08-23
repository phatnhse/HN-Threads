package com.phatnhse.hnthreads.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class HNSpacing(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 20.dp,
    val xl2: Dp = 24.dp,
    val xl3: Dp = 32.dp,
    val xl4: Dp = 40.dp,
    val xl5: Dp = 48.dp,
    val xl6: Dp = 64.dp,
    val xl7: Dp = 80.dp,
    val xl8: Dp = 96.dp
)

val DefaultSpacing = HNSpacing()

object HNDimensions {
    val minTouchTarget = 48.dp
    val cardElevation = 1.dp
    val cardRadius = 8.dp
    val buttonHeight = 44.dp
    val inputHeight = 48.dp
    val listItemHeight = 56.dp
}
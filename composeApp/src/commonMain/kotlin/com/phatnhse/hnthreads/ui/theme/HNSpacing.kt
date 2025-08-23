package com.phatnhse.hnthreads.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class HNSpacing(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,      // 0.25rem
    val sm: Dp = 8.dp,      // 0.5rem  
    val md: Dp = 12.dp,     // 0.75rem
    val lg: Dp = 16.dp,     // 1rem
    val xl: Dp = 20.dp,     // 1.25rem
    val xl2: Dp = 24.dp,    // 1.5rem
    val xl3: Dp = 32.dp,    // 2rem
    val xl4: Dp = 40.dp,    // 2.5rem
    val xl5: Dp = 48.dp,    // 3rem
    val xl6: Dp = 64.dp,    // 4rem
    val xl7: Dp = 80.dp,    // 5rem
    val xl8: Dp = 96.dp     // 6rem
)

val DefaultSpacing = HNSpacing()

// Mobile-optimized dimensions
object HNDimensions {
    val minTouchTarget = 48.dp
    val cardElevation = 1.dp
    val cardRadius = 8.dp
    val buttonHeight = 44.dp
    val inputHeight = 48.dp
    val listItemHeight = 56.dp
}
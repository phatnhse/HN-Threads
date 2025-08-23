package com.phatnhse.hnthreads.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

val LocalHNColors = compositionLocalOf<HNColors> { 
    error("No HNColors provided") 
}
val LocalHNSpacing = compositionLocalOf<HNSpacing> { 
    error("No HNSpacing provided") 
}
val LocalHNTypography = compositionLocalOf<HNTypography> { 
    error("No HNTypography provided") 
}

object HNTheme {
    val colors: HNColors
        @Composable
        @ReadOnlyComposable
        get() = LocalHNColors.current
        
    val spacing: HNSpacing
        @Composable  
        @ReadOnlyComposable
        get() = LocalHNSpacing.current
        
    val typography: HNTypography
        @Composable
        @ReadOnlyComposable  
        get() = LocalHNTypography.current
}

@Composable
fun HNTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    
    CompositionLocalProvider(
        LocalHNColors provides colors,
        LocalHNSpacing provides DefaultSpacing,
        LocalHNTypography provides DefaultTypography
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
            content = content
        )
    }
}
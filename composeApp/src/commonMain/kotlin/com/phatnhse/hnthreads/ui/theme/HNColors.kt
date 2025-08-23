package com.phatnhse.hnthreads.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class HNColors(
    // Primary colors
    val background: Color,
    val foreground: Color,
    val card: Color,
    val cardForeground: Color,
    val popover: Color,
    val popoverForeground: Color,
    val primary: Color,
    val primaryForeground: Color,
    val secondary: Color,
    val secondaryForeground: Color,
    val muted: Color,
    val mutedForeground: Color,
    val accent: Color,
    val accentForeground: Color,
    val destructive: Color,
    val destructiveForeground: Color,
    val border: Color,
    val input: Color,
    val ring: Color,
    // Additional colors for mobile
    val surfaceVariant: Color,
    val outline: Color,
    val onSurfaceVariant: Color
)

// Light theme colors
val LightColors = HNColors(
    background = Color(0xFFFFFFFF),           // Pure white
    foreground = Color(0xFF0F172A),           // slate-900
    card = Color(0xFFFFFFFF),                 // White
    cardForeground = Color(0xFF0F172A),       // slate-900
    popover = Color(0xFFFFFFFF),              // White
    popoverForeground = Color(0xFF0F172A),    // slate-900
    primary = Color(0xFF0F172A),              // slate-900
    primaryForeground = Color(0xFFF8FAFC),    // slate-50
    secondary = Color(0xFFF1F5F9),            // slate-100
    secondaryForeground = Color(0xFF0F172A),  // slate-900
    muted = Color(0xFFF1F5F9),                // slate-100
    mutedForeground = Color(0xFF64748B),      // slate-500
    accent = Color(0xFFF1F5F9),               // slate-100
    accentForeground = Color(0xFF0F172A),     // slate-900
    destructive = Color(0xFFEF4444),          // red-500
    destructiveForeground = Color(0xFFF8FAFC), // slate-50
    border = Color(0xFFE2E8F0),               // slate-200
    input = Color(0xFFE2E8F0),                // slate-200
    ring = Color(0xFF0F172A),                 // slate-900
    surfaceVariant = Color(0xFFF8FAFC),       // slate-50
    outline = Color(0xFF94A3B8),              // slate-400
    onSurfaceVariant = Color(0xFF475569)      // slate-600
)

// Dark theme colors
val DarkColors = HNColors(
    background = Color(0xFF0F172A),           // slate-900
    foreground = Color(0xFFF8FAFC),           // slate-50
    card = Color(0xFF0F172A),                 // slate-900
    cardForeground = Color(0xFFF8FAFC),       // slate-50
    popover = Color(0xFF0F172A),              // slate-900
    popoverForeground = Color(0xFFF8FAFC),    // slate-50
    primary = Color(0xFFF8FAFC),              // slate-50
    primaryForeground = Color(0xFF0F172A),    // slate-900
    secondary = Color(0xFF1E293B),            // slate-800
    secondaryForeground = Color(0xFFF8FAFC),  // slate-50
    muted = Color(0xFF1E293B),                // slate-800
    mutedForeground = Color(0xFF64748B),      // slate-500
    accent = Color(0xFF1E293B),               // slate-800
    accentForeground = Color(0xFFF8FAFC),     // slate-50
    destructive = Color(0xFF991B1B),          // red-800
    destructiveForeground = Color(0xFFF8FAFC), // slate-50
    border = Color(0xFF1E293B),               // slate-800
    input = Color(0xFF1E293B),                // slate-800
    ring = Color(0xFF94A3B8),                 // slate-400
    surfaceVariant = Color(0xFF1E293B),       // slate-800
    outline = Color(0xFF64748B),              // slate-500
    onSurfaceVariant = Color(0xFF94A3B8)      // slate-400
)
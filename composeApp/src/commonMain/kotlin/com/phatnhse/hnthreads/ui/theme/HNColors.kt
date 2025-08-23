package com.phatnhse.hnthreads.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class HNColors(
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
    val surfaceVariant: Color,
    val outline: Color,
    val onSurfaceVariant: Color
)

val LightColors = HNColors(
    background = Color(0xFFFFFFFF),
    foreground = Color(0xFF0F172A),
    card = Color(0xFFFFFFFF),
    cardForeground = Color(0xFF0F172A),
    popover = Color(0xFFFFFFFF),
    popoverForeground = Color(0xFF0F172A),
    primary = Color(0xFF0F172A),
    primaryForeground = Color(0xFFF8FAFC),
    secondary = Color(0xFFF1F5F9),
    secondaryForeground = Color(0xFF0F172A),
    muted = Color(0xFFF1F5F9),
    mutedForeground = Color(0xFF64748B),
    accent = Color(0xFFF1F5F9),
    accentForeground = Color(0xFF0F172A),
    destructive = Color(0xFFEF4444),
    destructiveForeground = Color(0xFFF8FAFC),
    border = Color(0xFFE2E8F0),
    input = Color(0xFFE2E8F0),
    ring = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF8FAFC),
    outline = Color(0xFF94A3B8),
    onSurfaceVariant = Color(0xFF475569)
)

val DarkColors = HNColors(
    background = Color(0xFF0F172A),
    foreground = Color(0xFFF8FAFC),
    card = Color(0xFF0F172A),
    cardForeground = Color(0xFFF8FAFC),
    popover = Color(0xFF0F172A),
    popoverForeground = Color(0xFFF8FAFC),
    primary = Color(0xFFF8FAFC),
    primaryForeground = Color(0xFF0F172A),
    secondary = Color(0xFF1E293B),
    secondaryForeground = Color(0xFFF8FAFC),
    muted = Color(0xFF1E293B),
    mutedForeground = Color(0xFF64748B),
    accent = Color(0xFF1E293B),
    accentForeground = Color(0xFFF8FAFC),
    destructive = Color(0xFF991B1B),
    destructiveForeground = Color(0xFFF8FAFC),
    border = Color(0xFF1E293B),
    input = Color(0xFF1E293B),
    ring = Color(0xFF94A3B8),
    surfaceVariant = Color(0xFF1E293B),
    outline = Color(0xFF64748B),
    onSurfaceVariant = Color(0xFF94A3B8)
)
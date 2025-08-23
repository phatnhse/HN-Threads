# Design System & Best Practices

## Theme Architecture

### HN Theme Structure
```kotlin
// Access theme values anywhere in Compose
HNTheme.colors.primary
HNTheme.spacing.lg  
HNTheme.typography.headlineSmall
```

### Components
- **HNColors** - Complete light/dark color tokens
- **HNSpacing** - Consistent spacing values (4dp to 96dp scale)
- **HNTypography** - Text styles for all use cases
- **HNTheme** - Main theme provider and accessor object

## Best Practices

### ❌ Never Use Hardcoded Values
```kotlin
// ❌ Avoid - Hardcoded values
Text(
    text = "Title",
    fontSize = 18.sp,
    color = Color.Blue,
    modifier = Modifier.padding(16.dp)
)

Box(
    modifier = Modifier
        .size(100.dp)
        .background(Color.Red)
)
```

### ✅ Always Use Theme System
```kotlin
// ✅ Good - Theme-based values
Text(
    text = "Title",
    style = HNTheme.typography.headlineSmall,
    color = HNTheme.colors.primary,
    modifier = Modifier.padding(HNTheme.spacing.md)
)

Box(
    modifier = Modifier
        .size(HNTheme.spacing.xl6)
        .background(HNTheme.colors.error)
)
```

## Color System

### Color Usage
```kotlin
// ✅ Semantic color names
HNTheme.colors.primary          // Main brand color
HNTheme.colors.onPrimary        // Text on primary
HNTheme.colors.surface          // Card backgrounds
HNTheme.colors.onSurface        // Text on surfaces
HNTheme.colors.error            // Error states
HNTheme.colors.onError          // Text on error

// ❌ Never use direct colors
Color.Blue, Color(0xFF1976D2), Color.Red
```

### Dark/Light Theme Support
```kotlin
// Theme automatically handles dark/light modes
@Composable
fun StoryCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = HNTheme.colors.surface,
            contentColor = HNTheme.colors.onSurface
        )
    ) {
        // Content adapts automatically
    }
}
```

## Typography Scale

### Text Styles
```kotlin
// ✅ Use predefined text styles
HNTheme.typography.displayLarge     // Hero text
HNTheme.typography.headlineLarge    // Page titles
HNTheme.typography.headlineMedium   // Section headers
HNTheme.typography.headlineSmall    // Subsection headers
HNTheme.typography.titleLarge       // Card titles
HNTheme.typography.titleMedium      // List item titles
HNTheme.typography.bodyLarge        // Article text
HNTheme.typography.bodyMedium       // Default body text
HNTheme.typography.bodySmall        // Captions, metadata
HNTheme.typography.labelLarge       // Button text
HNTheme.typography.labelMedium      // Form labels
HNTheme.typography.labelSmall       // Fine print
```

### Typography Best Practices
```kotlin
// ✅ Good - Semantic usage
Text(
    text = story.title,
    style = HNTheme.typography.titleLarge
)

Text(
    text = "${story.score} points",
    style = HNTheme.typography.bodySmall,
    color = HNTheme.colors.onSurfaceVariant
)

// ❌ Avoid - Custom font properties
Text(
    text = story.title,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 24.sp
)
```

## Spacing System

### Spacing Scale
```kotlin
HNTheme.spacing.xs      // 4dp  - Tight spacing
HNTheme.spacing.sm      // 8dp  - Small spacing
HNTheme.spacing.md      // 16dp - Default spacing
HNTheme.spacing.lg      // 24dp - Large spacing
HNTheme.spacing.xl      // 32dp - Extra large
HNTheme.spacing.xl2     // 40dp - 2x extra large
HNTheme.spacing.xl3     // 48dp - 3x extra large
HNTheme.spacing.xl4     // 56dp - 4x extra large
HNTheme.spacing.xl5     // 64dp - 5x extra large
HNTheme.spacing.xl6     // 80dp - 6x extra large
HNTheme.spacing.xl7     // 88dp - 7x extra large
HNTheme.spacing.xl8     // 96dp - 8x extra large
```

### Spacing Usage
```kotlin
// ✅ Good - Theme spacing
Column(
    modifier = Modifier.padding(HNTheme.spacing.md),
    verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.sm)
) {
    // Content
}

// ❌ Avoid - Magic numbers
Column(
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    // Content
}
```

## Component Guidelines

### Consistent Component API
```kotlin
// ✅ Good - Consistent modifier pattern
@Composable
fun StoryCard(
    story: Story,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = HNTheme.colors.surface
        )
    ) {
        // Content with theme values
    }
}
```

### Accessibility
```kotlin
// ✅ Always include accessibility
Text(
    text = "${story.score} points",
    style = HNTheme.typography.bodySmall,
    modifier = Modifier.semantics {
        contentDescription = "${story.score} points for this story"
    }
)

Button(
    onClick = onRefresh,
    modifier = Modifier.semantics {
        contentDescription = "Refresh stories"
    }
) {
    Icon(Icons.Default.Refresh, contentDescription = null)
}
```

## Responsive Design

### Adaptive Spacing
```kotlin
@Composable
fun ResponsiveLayout() {
    val configuration = LocalConfiguration.current
    val spacing = if (configuration.screenWidthDp > 600) {
        HNTheme.spacing.lg
    } else {
        HNTheme.spacing.md
    }
    
    Column(
        modifier = Modifier.padding(spacing)
    ) {
        // Content adapts to screen size
    }
}
```

## Resource Management

### String Resources
```kotlin
// ✅ Use string resources
Text(
    text = stringResource(R.string.stories_title),
    style = HNTheme.typography.headlineLarge
)

// ❌ Avoid hardcoded strings
Text(
    text = "Hacker News Stories",
    style = HNTheme.typography.headlineLarge
)
```

### Drawable Resources
```kotlin
// ✅ Use resource references
Icon(
    painter = painterResource(R.drawable.ic_comment),
    contentDescription = stringResource(R.string.comments_icon_desc),
    tint = HNTheme.colors.primary
)

// ❌ Avoid inline icons or hardcoded references
```

## Performance Considerations

### Theme Access
```kotlin
// ✅ Good - Access theme at composition time
@Composable
fun ThemedContent() {
    val colors = HNTheme.colors
    val typography = HNTheme.typography
    
    // Use throughout composable
}

// ❌ Avoid - Repeated theme access in loops
LazyColumn {
    items(stories) { story ->
        // Don't access HNTheme.colors here repeatedly
    }
}
```
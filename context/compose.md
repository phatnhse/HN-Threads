# Compose Best Practices

## State Management

### State Hoisting
- **Lift state up** to lowest common ancestor
- **Stateless composables** are preferred - pass data down, events up
- **Use `remember`** for expensive calculations only

### State Guidelines
```kotlin
// ✅ Good - Stateless composable
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) { }

// ❌ Avoid - Stateful composable
@Composable 
fun SearchBar() {
    var query by remember { mutableStateOf("") }
}
```

## Performance

### Stability
- **Use `@Stable` and `@Immutable`** for data classes
- **Avoid unstable parameters** in composables
- **Use `derivedStateOf`** for computed values

```kotlin
@Immutable
data class Story(
    val id: Long,
    val title: String,
    val score: Int
)
```

### Recomposition
- **Use `key()`** for dynamic lists
- **Prefer `LazyColumn`** over `Column` with many items
- **Split large composables** into smaller, focused ones

```kotlin
// ✅ Good - Use key for dynamic content
LazyColumn {
    items(stories, key = { it.id }) { story ->
        StoryItem(story = story)
    }
}
```

## Side Effects

### Effect Usage
- **`LaunchedEffect`** - One-time or keyed side effects
- **`DisposableEffect`** - Effects requiring cleanup
- **`SideEffect`** - Publish Compose state to non-compose code

```kotlin
@Composable
fun StoryScreen(storyId: Long, viewModel: StoryViewModel) {
    LaunchedEffect(storyId) {
        viewModel.loadStory(storyId)
    }
}
```

## Naming & Structure

### Composable Naming
- **PascalCase** for composables
- **Noun-based names** for UI elements
- **Verb-based names** for actions

### Parameters
- **Required parameters first**, optional parameters last
- **Modifier parameter last** (except trailing lambda)
- **Use default values** for optional parameters

```kotlin
@Composable
fun StoryCard(
    story: Story,
    onClick: () -> Unit,
    showScore: Boolean = true,
    modifier: Modifier = Modifier
) { }
```

## Layout & Theming

### Modifiers
- **Always accept Modifier** parameter
- **Apply modifier first** in composable chain
- **Don't use hard-coded sizes** - use theme values

```kotlin
@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(modifier = modifier) {
        content()
    }
}
```

### Theme Usage
- **Use theme colors/typography** instead of hard-coded values
- **Access theme via LocalComposition** or MaterialTheme
- **Consistent spacing** with theme spacing scale

```kotlin
// ✅ Good - Use theme values
Text(
    text = title,
    style = MaterialTheme.typography.headlineSmall,
    color = MaterialTheme.colorScheme.primary
)

// ❌ Avoid - Hard-coded values
Text(
    text = title,
    fontSize = 18.sp,
    color = Color.Blue
)
```

## Testing

### Composable Testing
- **Test UI state** not implementation
- **Use semantic properties** for assertions
- **Mock ViewModels** in UI tests

```kotlin
@Test
fun storyCard_displaysTitle() {
    composeTestRule.setContent {
        StoryCard(story = testStory, onClick = {})
    }
    
    composeTestRule
        .onNodeWithText(testStory.title)
        .assertIsDisplayed()
}
```

## Common Patterns

### Loading States
```kotlin
@Composable
fun StoriesScreen(uiState: StoriesUiState) {
    when (uiState) {
        is Loading -> LoadingIndicator()
        is Success -> StoriesList(uiState.stories)
        is Error -> ErrorMessage(uiState.message)
    }
}
```

### Conditional Composition
```kotlin
// ✅ Good - Early return
@Composable
fun UserProfile(user: User?) {
    if (user == null) {
        Text("No user found")
        return
    }
    
    Column {
        Text(user.name)
        Text(user.email)
    }
}
```
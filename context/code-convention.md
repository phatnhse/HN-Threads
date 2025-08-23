# Code Convention

## Naming Conventions

### Classes & Interfaces
- **PascalCase** for classes: `StoriesViewModel`, `HackerNewsRepository`
- **Interface names** without "I" prefix: `Repository` not `IRepository`
- **Impl suffix** for implementations: `StoriesRepositoryImpl`

### Functions & Variables  
- **camelCase** for functions and variables: `refreshStories()`, `uiState`
- **Boolean variables** with descriptive names: `isLoading`, `hasError`
- **Constant values** in SCREAMING_SNAKE_CASE: `DEFAULT_TIMEOUT`, `MAX_RETRY_COUNT`

### Packages
- **Lowercase** with dots: `com.phatnhse.hnthreads.features.stories`
- **Feature-based** organization: `features/stories/presentation`, `features/comments/data`

## Code Organization

### File Structure
```kotlin
// Single responsibility - one public class per file
class StoriesViewModel() { }

// File name matches class name: StoriesViewModel.kt
```

### Import Organization
```kotlin
// 1. Standard library imports
import kotlin.collections.List

// 2. Third-party library imports  
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel

// 3. Project imports
import com.phatnhse.hnthreads.shared.data.models.Story
```

## Kotlin Best Practices

### Data Classes
```kotlin
// Immutable data classes
data class Story(
    val id: Long,
    val title: String,
    val score: Int
)

// Use default parameters instead of overloads
data class Comment(
    val id: Long,
    val text: String,
    val depth: Int = 0
)
```

### Null Safety
```kotlin
// Prefer Elvis operator
val title = story.title ?: "Unknown"

// Use safe calls
story.comments?.size

// Avoid !! operator except when absolutely certain
```

### Extension Functions
```kotlin
// Keep extensions close to usage
fun String.toTimeAgo(): String = "2 hours ago"

// Prefer extensions over utility classes
fun List<Story>.filterByScore(minScore: Int) = filter { it.score >= minScore }
```

## Dependency Injection
```kotlin
// Constructor injection preferred
class StoriesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : StoriesRepository

// Avoid field injection or service locator patterns
```

## Error Handling

### Exception Handling
```kotlin
// Specific exception types
try {
    repository.refreshStories()
} catch (networkException: NetworkException) {
    _uiState.value = StoriesUiState.NetworkError
} catch (exception: Exception) {
    _uiState.value = StoriesUiState.Error(exception.message)
}
```

### Direct Exception Throwing
```kotlin
// Throw exceptions directly, let callers handle them
suspend fun refreshStories() {
    // Throws NetworkException, DatabaseException, etc.
    val stories = remoteDataSource.getStories()
    localDataSource.saveStories(stories)
}

// Callers handle exceptions with try-catch
viewModelScope.launch {
    try {
        repository.refreshStories()
    } catch (exception: Exception) {
        _uiState.value = StoriesUiState.Error(exception.message)
    }
}
```

## Testing Conventions

### Test Structure
```kotlin
class StoriesViewModelTest {
    @Test
    fun `refresh stories updates ui state correctly`() = runTest {
        // Given
        val repository = mockk<StoriesRepository>()
        val viewModel = StoriesViewModel(repository)
        
        // When
        viewModel.refresh()
        
        // Then
        verify { repository.refreshStories() }
    }
}
```

## Documentation

### KDoc Comments
```kotlin
/**
 * Repository for managing Hacker News stories.
 * 
 * Follows local-first architecture where UI always reads from local storage.
 */
interface StoriesRepository {
    /**
     * Returns a flow of stories from local storage.
     * UI should observe this flow for reactive updates.
     */
    fun getStoriesFlow(): Flow<List<Story>>
}
```

### Code Comments
```kotlin
// Avoid obvious comments
val stories = repository.getStories() // Gets stories ❌

// Explain why, not what
viewModelScope.launch {
    // Trigger background refresh but don't block UI
    repository.refreshStories()
}
```

## Resource Management

### Theme Usage
```kotlin
// Always use theme values, never hardcoded
Text(
    text = story.title,
    style = HNTheme.typography.headlineSmall, // ✅
    color = HNTheme.colors.foreground        // ✅
)

// Avoid hardcoded values
Text(
    text = story.title,
    fontSize = 16.sp,  // ❌
    color = Color.Black // ❌
)
```

### String Resources
```kotlin
// Use string resources for user-facing text
Text(text = stringResource(R.string.loading)) // ✅

// Avoid hardcoded strings
Text(text = "Loading...") // ❌
```
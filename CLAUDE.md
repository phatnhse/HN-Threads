# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Kotlin Multiplatform Mobile (KMM)** project using **Compose Multiplatform** targeting Android and iOS.

- **Application ID**: `com.phatnhse.hnthreads`
- **Project Name**: `hnthreads`

## References

- For detailed feature specifications and business requirements, see [FEATURES.md](./context/features.md).
- For Hacker News API documentation and endpoint specifications, see [API.md](./context/api.md).
- For Compose development best practices and guidelines, see [COMPOSE.md](./context/compose.md).
- For design system guidelines and theming best practices, see [DESIGN-SYSTEM.md](./context/design-system.md).
- For Android code conventions and best practices, see [CODE-CONVENTION.md](./context/code-convention.md).

## Architecture

### Multi-Module
```
hnthreads/
├── composeApp/                      # Main application module
│   └── src/
│       ├── androidMain/             # Android app entry point
│       ├── iosMain/                 # iOS app entry point  
│       └── commonMain/              # App.kt, DI setup
├── shared/                          # Core shared module
│   └── src/commonMain/kotlin/com/phatnhse/hnthreads/shared/
│       ├── data/                    # API, database, models
│       ├── ui/                      # Components, theme, navigation
│       └── utils/                   # Extensions, helpers
├── feature-stories/                 # Stories feature module
│   └── src/commonMain/kotlin/com/phatnhse/hnthreads/features/stories/
│       ├── data/                    # Repository implementation
│       ├── presentation/            # ViewModel, UI state
│       └── ui/                      # Composables
├── feature-comments/                # Comments feature module
├── feature-user/                    # User feature module
└── feature-settings/                # Settings feature module
```

**Rules**: Feature modules depend only on shared module, not other features. Cross-feature communication via shared repositories.

### MVVM + Repository Pattern

#### Core Principles
- **Reactive Data Flow** - Reactive state management for UI updates
- **Separation of Concerns** - Data (repository), ViewModel (state), View (UI)
- **Local-First** - Remote updates local storage, UI observes local storage

#### MVVM Architecture Layers
```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐     ┌────────────┐
│  UI Layer   │ ──▶ │ Presentation │ ──▶ │ Repository  │ ──▶ │ Datasource │
└─────────────┘     └──────────────┘     └─────────────┘     └────────────┘
```

#### Reactive View-ViewModel Flow
```
┌─────────────────┐                    ┌─────────────────┐
│                 │◀───── Events ──────│                 │
│    ViewModel    │                    │      View       │
│                 │────── State ──────▶│                 │
└─────────────────┘                    └─────────────────┘
```

### Layer Responsibilities

#### **Data Layer**
```kotlin
// Repository Pattern Implementation
interface StoriesRepository {
    fun getStoriesFlow(): Flow<List<Story>>
    suspend fun refreshStories()
}

class StoriesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : StoriesRepository {
    
    // UI always gets data from local storage
    override fun getStoriesFlow(): Flow<List<Story>> = localDataSource.getStoriesFlow()
    
    // Background sync updates local storage
    override suspend fun refreshStories() {
        val stories = remoteDataSource.getStories()
        localDataSource.saveStories(stories) // Local storage notifies observers
    }
}
```

#### **Presentation Layer**
```kotlin
// ViewModel consumes repository directly
class StoriesViewModel(
    private val repository: StoriesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(StoriesUiState.Loading)
    val uiState: StateFlow<StoriesUiState> = _uiState.asStateFlow()
    
    init {
        // Reactive data binding
        viewModelScope.launch {
            repository.getStoriesFlow().collect { stories ->
                _uiState.value = StoriesUiState.Success(stories)
            }
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            repository.refreshStories() // Updates database, UI reacts automatically
        }
    }
}
```

#### **UI Layer**
```kotlin
@Composable
fun StoriesScreen(viewModel: StoriesViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is StoriesUiState.Loading -> LoadingIndicator()
        is StoriesUiState.Success -> StoriesList(
            stories = uiState.stories,
            onRefresh = viewModel::refresh
        )
        is StoriesUiState.Error -> ErrorMessage()
    }
}
```


## Development Commands

### Building & Running

```bash
# Clean the project
./gradlew clean

# Build all targets
./gradlew build

# Build Android debug APK  
./gradlew composeApp:assembleDebug

# Build Android release APK
./gradlew composeApp:assembleRelease

# Run Android app (with connected device/emulator)
./gradlew composeApp:installDebug

# Build iOS framework for Xcode
./gradlew composeApp:embedAndSignAppleFrameworkForXcode
```

### Testing

```bash  
# Run all tests
./gradlew test

# Run specific platform tests
./gradlew composeApp:testDebugUnitTest        # Android tests
./gradlew composeApp:iosX64Test               # iOS simulator tests

# Run tests with coverage
./gradlew testDebugUnitTestCoverage
```

### iOS Development

The iOS app requires Xcode for building and running:

1. Build the iOS framework: `./gradlew composeApp:embedAndSignAppleFrameworkForXcode`
2. Open `iosApp/iosApp.xcodeproj` in Xcode
3. Build and run from Xcode

## Tech Stack

- **Kotlin 2.2.0** - Primary development language
- **Compose Multiplatform 1.8.2** - Cross-platform UI framework  
- **Android Gradle Plugin 8.7.3** - Android build system
- **Room** - Multiplatform database
- **Kotlin Serialization** - JSON parsing
- **DataStore** - Preferences storage
- **Ktor Client** - HTTP networking
- **Koin** - Dependency injection
- **Coroutines** - Async programming
- **Compose Navigation** - Screen navigation
- **Coil** - Image loading
- **DateTime** - Date/time utilities
- **Kotlin Test** - Testing framework
- **Turbine** - Flow testing
- **MockK** - Mocking
- **Napier** - Logging

## Development Guidelines

### Code Organization
- Place shared UI and business logic in `commonMain`
- Use `androidMain` only for Android-specific APIs (sensors, platform services)
- Use `iosMain` only for iOS-specific APIs (CoreData, iOS-specific features)
- Follow Compose best practices for state management

### Platform Abstractions  
- Define interfaces in `commonMain/Platform.kt`
- Implement platform-specific versions in respective `Platform.xxx.kt` files
- Use `expect`/`actual` declarations for platform-specific functionality

### Testing Strategy
- Write shared tests in `commonTest` for business logic
- Platform-specific tests go in `androidTest` and `iosTest` respectively  
- Use `kotlin-test` for multiplatform test compatibility

## Git Conventions

### Commit Message Format

Use the following format for consistent, readable commit messages:

```
<type>(<scope>): <description>

[optional body]
```

### Examples

```bash
# Feature additions
feat/add-ktor: add Hacker News API client with Ktor
feat/enhance-threading-layout: implement enhanced comment threading layout
feat/add-pull-to-refresh: add pull-to-refresh gesture support
```

### Best Practices

- **Keep subject line under 50 characters**
- **Use imperative mood** ("add" not "added" or "adds")
- **Capitalize first letter** of description
- **No period at end** of subject line
- **Be specific** about what changed and why

### Branch Naming

```bash
# Feature branches
feature/story-detail-screen
feature/comment-threading
feature/offline-sync

# Bug fixes  
fix/crash-on-story-load
fix/duplicate-comments

# Refactor  
refactor/relocate-interceptors
```

## Resources & Documentation

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform Guide](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Android Developer Docs](https://developer.android.com/develop/ui/compose)
- [iOS Integration Guide](https://kotlinlang.org/docs/native-ios-integration.html)
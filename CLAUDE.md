# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Kotlin Multiplatform Mobile (KMM)** project using **Compose Multiplatform** targeting Android and iOS. The project structure follows KMM conventions with shared code in `commonMain` and platform-specific implementations in `androidMain` and `iosMain`.

**Application ID**: `com.phatnhse.hnthreads`
**Project Name**: hnthreads

## Application Purpose

**HN Threads** is a cross-platform Hacker News client built with Kotlin Multiplatform and Compose Multiplatform. For detailed feature specifications and business requirements, see [FEATURES.md](./FEATURES.md).

## Architecture

### Module Structure
- **`/composeApp`** - Main multiplatform module containing shared and platform-specific code
  - `src/commonMain/kotlin` - Shared business logic and UI components
  - `src/androidMain/kotlin` - Android-specific implementations  
  - `src/iosMain/kotlin` - iOS-specific implementations
  - `src/commonTest/kotlin` - Shared unit tests
- **`/iosApp`** - iOS application entry point with SwiftUI wrapper
/cost
### Key Source Sets

```
composeApp/src/
├── commonMain/kotlin/com/phatnhse/hnthreads/
│   ├── App.kt                    # Main Compose UI entry point
│   ├── Greeting.kt               # Shared greeting logic
│   └── Platform.kt               # Platform abstraction
├── androidMain/kotlin/com/phatnhse/hnthreads/
│   ├── MainActivity.kt           # Android activity
│   └── Platform.android.kt       # Android platform implementation  
└── iosMain/kotlin/com/phatnhse/hnthreads/
    ├── MainViewController.kt     # iOS view controller factory
    └── Platform.ios.kt           # iOS platform implementation
```

## App Architecture

### MVVM + Repository Pattern

The app follows **MVVM (Model-View-ViewModel)** architecture with **Repository Pattern** and **Reactive Programming** principles to ensure scalable, testable, and maintainable code.

### Core Architectural Principles

#### 1. Room as Single Source of Truth
- **All UI data flows from Room database** - Never display data directly from network calls
- **Database-first approach** - UI always reflects the current database state
- **Offline-first design** - App remains functional without network connectivity

#### 2. Reactive Data Flow
- **StateFlow/SharedFlow** for reactive state management
- **Flow-based data streams** from Repository to ViewModel to UI  
- **Automatic UI updates** when database changes occur
- **Declarative UI** with Compose reacting to state changes

#### 3. Separation of Concerns
- **Repository handles data operations** (network + database)
- **ViewModel manages UI state** and business logic
- **Composables are pure functions** of state
- **Use cases encapsulate** specific business operations

### Data Flow Architecture

```
┌─────────────┐    ┌──────────────┐    ┌─────────────┐    ┌─────────────┐
│   Network   │───▶│  Repository  │───▶│  Database   │───▶│     UI      │
│   (Ktor)    │    │   (Single    │    │   (Room)    │    │ (Compose)   │
│             │    │    Source)   │    │             │    │             │
└─────────────┘    └──────────────┘    └─────────────┘    └─────────────┘
                           │                    ▲               ▲
                           │                    │               │
                           ▼                    │               │
                   ┌──────────────┐            │               │
                   │  Background  │────────────┘               │
                   │ Sync/Refresh │                            │
                   └──────────────┘                            │
                                                               │
                   ┌──────────────┐                            │
                   │  ViewModel   │────────────────────────────┘
                   │ (StateFlow)  │
                   └──────────────┘
```

### Layer Responsibilities

#### **Data Layer**
```kotlin
// Repository Pattern Implementation
interface HackerNewsRepository {
    fun getStoriesFlow(): Flow<List<Story>>
    fun getCommentsFlow(storyId: Long): Flow<List<Comment>>
    suspend fun refreshStories()
    suspend fun refreshComments(storyId: Long)
}

class HackerNewsRepositoryImpl(
    private val api: HackerNewsApi,
    private val dao: HackerNewsDao
) : HackerNewsRepository {
    
    // UI always gets data from database
    override fun getStoriesFlow(): Flow<List<Story>> = dao.getAllStoriesFlow()
    
    // Background sync updates database
    override suspend fun refreshStories() {
        val stories = api.getTopStories()
        dao.insertStories(stories) // Room notifies observers
    }
}
```

#### **Domain Layer**
```kotlin
// Use Cases encapsulate business logic
class GetStoriesUseCase(
    private val repository: HackerNewsRepository
) {
    operator fun invoke(): Flow<List<Story>> = repository.getStoriesFlow()
}

class RefreshStoriesUseCase(
    private val repository: HackerNewsRepository
) {
    suspend operator fun invoke() = repository.refreshStories()
}
```

#### **Presentation Layer**
```kotlin
// ViewModel manages UI state reactively
class StoriesViewModel(
    private val getStories: GetStoriesUseCase,
    private val refreshStories: RefreshStoriesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(StoriesUiState.Loading)
    val uiState: StateFlow<StoriesUiState> = _uiState.asStateFlow()
    
    init {
        // Reactive data binding
        viewModelScope.launch {
            getStories().collect { stories ->
                _uiState.value = StoriesUiState.Success(stories)
            }
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            refreshStories() // Updates database, UI reacts automatically
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

### Key Implementation Patterns

#### 1. **Never Direct Network-to-UI**
```kotlin
// ❌ Wrong - Direct network call in UI
@Composable
fun StoriesScreen() {
    var stories by remember { mutableStateOf<List<Story>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        stories = api.getStories() // Direct network call
    }
}

// ✅ Correct - Database-driven UI
@Composable  
fun StoriesScreen(viewModel: StoriesViewModel) {
    val stories by viewModel.storiesFlow.collectAsState(initial = emptyList())
    // stories comes from database via Flow
}
```

#### 2. **Reactive State Updates**
```kotlin
// Repository automatically updates UI when database changes
class RepositoryImpl : Repository {
    override fun getCommentsFlow(storyId: Long): Flow<List<Comment>> {
        return dao.getCommentsFlow(storyId) // Room Flow
            .onStart { refreshComments(storyId) } // Trigger background refresh
    }
    
    private suspend fun refreshComments(storyId: Long) {
        val comments = api.getComments(storyId)
        dao.insertComments(comments) // This triggers Flow emission
    }
}
```

#### 3. **Background Data Sync**
```kotlin
// Separate background operations from UI updates
class DataSyncManager(
    private val repository: HackerNewsRepository,
    private val scope: CoroutineScope
) {
    fun startPeriodicSync() {
        scope.launch {
            while (true) {
                repository.refreshStories()
                repository.refreshComments()
                delay(15.minutes) // Background refresh
            }
        }
    }
}
```

### Architecture Benefits

- **Offline-First**: App works without network, shows cached data
- **Reactive UI**: Automatic updates when data changes
- **Testable**: Each layer can be unit tested independently  
- **Scalable**: Clear separation allows easy feature additions
- **Consistent State**: Single source of truth eliminates data inconsistencies
- **Performance**: Database queries are fast, network calls don't block UI

### Dependency Injection Structure

```kotlin
// Koin modules following architecture layers
val dataModule = module {
    single<HackerNewsApi> { HackerNewsApiImpl(get()) }
    single<HackerNewsDao> { get<AppDatabase>().hackerNewsDao() }
    single<HackerNewsRepository> { HackerNewsRepositoryImpl(get(), get()) }
}

val domainModule = module {
    factory { GetStoriesUseCase(get()) }
    factory { RefreshStoriesUseCase(get()) }
}

val presentationModule = module {
    viewModel { StoriesViewModel(get(), get()) }
    viewModel { CommentsViewModel(get(), get()) }
}
```

This architecture ensures that the HN Threads app maintains a clear, reactive, and scalable codebase while providing excellent user experience through offline-first design.

## Design System

The app uses a **shadcn/ui-inspired design system** built with **CompositionLocalProvider** for consistent theming across all screens. The design system provides:

### Key Features
- **Light and Dark themes** with shadcn/ui color palette (slate-based colors)
- **Spacing system** following Tailwind CSS conventions (xs: 4dp to xl8: 96dp)  
- **Typography scale** with proper font weights and line heights for mobile
- **Mobile-optimized** touch targets and accessibility features
- **CompositionLocal providers** for type-safe theme access throughout the app

### Usage Pattern
```kotlin
// Access theme values anywhere in Compose
HNTheme.colors.primary
HNTheme.spacing.lg  
HNTheme.typography.headlineSmall
```

### Components Structure
- **HNColors** - Complete light/dark color tokens
- **HNSpacing** - Consistent spacing values
- **HNTypography** - Text styles for all use cases
- **HNTheme** - Main theme provider and accessor object

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

## Technology Stack

### Core Technologies
- **Kotlin 2.2.0** - Primary development language
- **Compose Multiplatform 1.8.2** - Cross-platform UI framework  
- **Android Gradle Plugin 8.7.3** - Android build system
- **Kotlin Multiplatform** - Code sharing between platforms

### Dependencies (from libs.versions.toml)
- **androidx.activity:activity-compose** - Android Compose integration
- **androidx.lifecycle:lifecycle-viewmodel-compose** - ViewModel integration
- **androidx.lifecycle:lifecycle-runtime-compose** - Lifecycle-aware composables
- **org.jetbrains.compose** - Compose Multiplatform runtime
- **kotlin-test** - Testing framework

### Build Configuration
- **Compile SDK**: 35
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 35
- **JVM Target**: 11

## Planned Tech Stack

This section outlines the multiplatform libraries and dependencies that will be used to build the complete HN Threads application. All listed dependencies have full Kotlin Multiplatform support.

### Core Architecture & DI
- **Koin 3.5+** - Dependency injection framework with excellent KMP support
  - `io.insert-koin:koin-core` - Core DI functionality  
  - `io.insert-koin:koin-compose` - Compose integration
  - `io.insert-koin:koin-test` - Testing utilities

### Data Layer
- **Room 2.6+** - Local database with multiplatform support
  - `androidx.room:room-runtime` - Core Room database
  - `androidx.room:room-ktx` - Kotlin extensions and Coroutines support
  - `androidx.room:room-compiler` - Annotation processor
- **Kotlin Serialization** - JSON parsing and serialization
  - `org.jetbrains.kotlinx:kotlinx-serialization-json` - JSON serialization
- **DataStore** - Modern preferences/settings storage
  - `androidx.datastore:datastore-preferences-core` - Multiplatform preferences

### Network Layer  
- **Ktor Client 2.3+** - HTTP client with full multiplatform support
  - `io.ktor:ktor-client-core` - Core HTTP client
  - `io.ktor:ktor-client-content-negotiation` - JSON content negotiation
  - `io.ktor:ktor-serialization-kotlinx-json` - Kotlinx.serialization integration
  - `io.ktor:ktor-client-logging` - HTTP request/response logging
  - Platform-specific engines:
    - `io.ktor:ktor-client-okhttp` - Android HTTP engine  
    - `io.ktor:ktor-client-darwin` - iOS HTTP engine

### Async & Concurrency
- **Kotlinx Coroutines** - Asynchronous programming
  - `org.jetbrains.kotlinx:kotlinx-coroutines-core` - Core coroutines support
  - `org.jetbrains.kotlinx:kotlinx-coroutines-test` - Testing utilities

### Navigation
- **Compose Navigation** - Multiplatform navigation
  - `org.jetbrains.androidx.navigation:navigation-compose` - Navigation for Compose Multiplatform

### State Management  
- **ViewModel** - UI state management
  - `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose` - ViewModel with Compose
- **StateFlow/SharedFlow** - Reactive state management (part of coroutines)

### Image Loading
- **Coil3** - Image loading with multiplatform support
  - `io.coil-kt.coil3:coil` - Core image loading
  - `io.coil-kt.coil3:coil-compose` - Compose integration
  - `io.coil-kt.coil3:coil-network-ktor` - Ktor network integration

### Date/Time
- **Kotlinx DateTime** - Multiplatform date/time handling
  - `org.jetbrains.kotlinx:kotlinx-datetime` - Date/time utilities

### Testing
- **Kotlin Test** - Multiplatform testing framework
  - `org.jetbrains.kotlin:kotlin-test` - Test framework
- **Turbine** - Testing Flow/StateFlow
  - `app.cash.turbine:turbine` - Flow testing utilities
- **MockK** - Mocking framework
  - `io.mockk:mockk` - Mocking for unit tests
- **Koin Test** - DI testing utilities
  - `io.insert-koin:koin-test` - Testing Koin modules

### Development Tools
- **Napier** - Multiplatform logging
  - `io.github.aakira:napier` - Structured logging across platforms
- **BuildKonfig** - Multiplatform build configuration
  - `com.codingfeline.buildkonfig:buildkonfig-gradle-plugin` - Build-time configuration

### Version Reference

```kotlin
// In gradle/libs.versions.toml
[versions]
koin = "3.5.3"
room = "2.6.1" 
ktor = "2.3.7"
coroutines = "1.7.3"
serialization = "1.6.2"
datastore = "1.1.0"
coil = "3.0.0"
datetime = "0.5.0"
napier = "2.7.1"
```

### Architecture Benefits

This tech stack provides:
- **100% Code Sharing**: Business logic, data models, and networking shared between platforms
- **Type Safety**: Kotlin's type system across the entire stack
- **Modern Async**: Coroutines-first approach with Flow for reactive programming  
- **Testability**: Comprehensive testing support with shared test code
- **Performance**: Native performance on both platforms with efficient data handling

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

## Common Tasks

### Adding Dependencies

1. Update `gradle/libs.versions.toml` with version numbers
2. Add to `[libraries]` section with proper module reference  
3. Reference in `composeApp/build.gradle.kts` within appropriate `sourceSets`

### Creating New Screens
- Add Composables in `commonMain/kotlin` 
- Use Material3 theming (`MaterialTheme.colorScheme`)
- Follow Compose state hoisting patterns
- Consider using `remember` for stateful operations

### Platform-Specific Features
1. Define `expect` function in `commonMain/Platform.kt`
2. Implement `actual` function in `androidMain/Platform.android.kt` 
3. Implement `actual` function in `iosMain/Platform.ios.kt`

## Build Troubleshooting

### Common Issues
- **iOS build fails**: Ensure Xcode is updated and iOS framework is generated first
- **Android build fails**: Check SDK versions in `local.properties`
- **Compose resources not found**: Clean and rebuild project
- **Gradle sync issues**: Clear Gradle cache with `./gradlew clean`

### Performance
- Use `@Stable` and `@Immutable` annotations for Compose optimization
- Minimize recomposition with proper state management
- Consider lazy loading for large lists using `LazyColumn`

## Git Conventions

### Commit Message Format

Use the following format for consistent, readable commit messages:

```
<type>(<scope>): <description>

[optional body]
```

### Types
- **feat**: New feature or enhancement
- **fix**: Bug fix  
- **refactor**: Code refactoring without functional changes
- **ui**: UI/UX changes, styling, or layout updates
- **deps**: Dependency updates or changes
- **docs**: Documentation updates
- **test**: Adding or updating tests
- **build**: Build system or CI/CD changes
- **perf**: Performance improvements

### Scopes (optional)
- **android**: Android-specific changes
- **ios**: iOS-specific changes  
- **common**: Shared/common code changes
- **api**: API or networking changes
- **db**: Database or data layer changes
- **ui**: UI components or screens
- **theme**: Design system or theming changes

### Examples

```bash
# Feature additions
feat(common): add Hacker News API client with Ktor
feat(ui): implement enhanced comment threading layout
feat(android): add pull-to-refresh gesture support

# Bug fixes
fix(ios): resolve crash when loading comments
fix(common): handle null values in story parsing
fix(db): prevent duplicate story entries

# UI/Design changes  
ui(theme): update color palette to match shadcn/ui
ui(common): improve story card layout and spacing
ui(android): fix navigation bar styling

# Refactoring
refactor(common): extract comment threading logic to use case
refactor(db): simplify Room entity relationships

# Dependencies
deps: update Compose Multiplatform to 1.8.2
deps: add Koin dependency injection framework
```

### Best Practices

- **Keep subject line under 50 characters**
- **Use imperative mood** ("add" not "added" or "adds")
- **Capitalize first letter** of description
- **No period at end** of subject line
- **Include issue references** when applicable: `fixes #123`
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

# UI improvements
ui/redesign-story-cards
ui/dark-theme-improvements
```

## Project Structure Philosophy

This project follows Kotlin Multiplatform conventions:
- **Shared-first approach**: Maximize code sharing in `commonMain`
- **Platform parity**: Ensure consistent behavior across platforms  
- **Compose-centric UI**: Use Compose for all UI rendering
- **Gradle convention**: Use `libs.versions.toml` for dependency management

## Resources & Documentation

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform Guide](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Android Developer Docs](https://developer.android.com/develop/ui/compose)
- [iOS Integration Guide](https://kotlinlang.org/docs/native-ios-integration.html)
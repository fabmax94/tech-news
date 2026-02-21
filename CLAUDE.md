# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (minified)
./gradlew assembleRelease

# Install on connected device/emulator
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run a single test class
./gradlew test --tests "com.fabmax.technews.ExampleUnitTest"
```

The `local.properties` file (git-ignored) must define:
```
sdk.dir=/Users/<user>/Library/Android/sdk
```

## Architecture

Clean Architecture with three layers:

**Domain** (`domain/`) — pure Kotlin, no Android dependencies.
- `model/Article.kt` — core data class; `ArticleCategory` enum drives category filtering throughout the app.
- `repository/NewsRepository.kt` — interface contract; all UI/VM code depends only on this interface.
- `usecase/` — one use case per operation (`GetArticlesUseCase`, `RefreshArticlesUseCase`, `ToggleBookmarkUseCase`, `SearchArticlesUseCase`).

**Data** (`data/`) — implements domain interfaces.
- `remote/RssParser.kt` — custom XML pull parser; auto-detects RSS 2.0 vs Atom by checking for `<feed` tag. Uses Jsoup for HTML cleaning and image extraction.
- `remote/RssFeedService.kt` — single Retrofit interface using `@Url` for dynamic feed URLs (base URL is a placeholder).
- `local/` — Room database (`AppDatabase`) with two DAOs: `ArticleDao` and `SourceDao`.
- `model/DefaultSources.kt` — hardcoded list of 15 RSS sources seeded on first run.
- `repository/NewsRepositoryImpl.kt` — fetches all enabled sources in parallel with `async/awaitAll`. Articles are cached for 7 days; non-bookmarked articles older than that are purged on each refresh.

**Presentation** (`presentation/`) — Jetpack Compose + MVVM.
- `navigation/` — `Screen` sealed class with URL-encoded route helpers; `TechNewsNavHost` wires all screens.
- `ui/home/HomeViewModel` — uses `flatMapLatest` to reactively switch the article stream when category changes; search uses `debounce(300ms)`.
- `ui/theme/CategoryColor.kt` — maps `ArticleCategory` to a `Color`; must stay in sync with the enum.

**DI** (`di/`) — three Hilt modules: `AppModule` (binds repository interface), `DatabaseModule`, `NetworkModule`.

**Worker** — `RefreshNewsWorker` runs every 2 hours via `WorkManager` (requires network); configured in `TechNewsApplication`.

## Key Conventions

- `ArticleCategory` is stored in Room as its `name` string (e.g. `"AI"`). All conversions use `ArticleCategory.valueOf(string)` with a fallback to `GENERAL`.
- Article IDs are derived from `"${sourceId}_${guid}"` or a UUID from `title+link` when no guid is present, ensuring insert-or-ignore deduplication.
- New news sources are added to `DefaultSources.all`. The `category` field must be `ArticleCategory.<VALUE>.name`.
- `RssParser` is a singleton injected via Hilt; it is thread-safe (no mutable state).
- The `ArticleScreen` does not render full article HTML — it shows the description and opens the original URL in the browser.

# Fuse CSGO Matches

Fuse CSGO Matches is an Android application that displays a list of Counter-Strike: Global
Offensive (CS:GO) matches.

## Implemented Features (Brief)

*(You can expand this section with more details about specific features implemented)*

* Displays a list of CS:GO matches.
* Shows details for a selected match.
* **Localization**: Supports both English (en) and Portuguese (pt) languages.

## Tech Stack & Architectural Decisions

This project is built using modern Android development practices and libraries.
Using Android Studio Narwhal | 2025.1.1 Patch 1

### Core Architecture

* **Modular Design**: The project is structured into multiple modules (`app`, `feature-core`,
  `feature-main`, `feature-details`) to promote separation of concerns, scalability, and
  maintainability.
* **MVI (Model-View-Intent)**: This architectural pattern is used to structure the presentation
  layer, promoting a unidirectional data flow and clear separation of concerns. It typically
  involves managing state, handling user intents, and updating the UI accordingly.
* **Dependency Injection (Hilt)**: Uses Dagger Hilt for managing dependencies throughout the
  application, simplifying the setup and providing a standard way to perform DI.
* **100% Kotlin**: The entire project is written in Kotlin.
* **Dependency Management**: Uses Gradle Version Catalogs (`libs.versions.toml`) for
  centralized and easier management of dependency versions.

### UI

* **Jetpack Compose**: The UI is built entirely with Jetpack Compose, Android's modern
  declarative UI toolkit.
    * **Navigation (androidx.navigation3)**: Utilized for navigating between different screens,
      integrated with Jetpack Compose.
    * **Material 3**: Implements Material Design 3 components and theming.
    * **ConstraintLayout for Compose**: Utilized for more complex layouts where
      ConstraintLayout's capabilities are beneficial.
* **Coil**: For efficient image loading in Compose.
* **AndroidX Core Splashscreen**: Implements the modern Android splash screen.

### Networking & Data

* **Retrofit 3**: For type-safe HTTP client communication with a backend API.
* **OkHttp 3**: As the underlying HTTP client for Retrofit, including a logging
  interceptor for easier debugging.
* **Kotlinx Serialization**: For efficient JSON parsing (serialization/deserialization).

### Asynchronous Programming

* **Kotlin Coroutines**: Used extensively for managing background tasks and asynchronous
  operations, integrated with AndroidX Lifecycle components.

### Other Key Libraries

* **AndroidX Lifecycle**: For lifecycle-aware components (ViewModels, LiveData observation
  with Compose).
* **Unit & Instrumented Testing**:
    * **JUnit 4**: For local unit tests.
    * **AndroidX Test (JUnit KTX, Espresso)**: For instrumented tests.

## Prerequisites

* **Java Development Kit (JDK) 21**: This project requires JDK 21. Please ensure it is
  installed and configured in your system and Android Studio. You can download it from
  [Oracle's website](https://www.oracle.com/java/technologies/downloads/#java21)
  or use a distribution like OpenJDK.

## Notes

* **Screen Orientation (Portrait Lock):**
    * **Decision:** The application is intentionally locked to Portrait mode.
    * **Rationale:** This was a deliberate choice to streamline development for this challenge,
      allowing for a more focused implementation of the requested features without the added
      complexity of adaptive layouts for multiple orientations.
    * **Implementation:** Portrait mode is enforced programmatically within Jetpack Compose. The
      `Activity.requestedOrientation` is set to `ActivityInfo.SCREEN_ORIENTATION_PORTRAIT` at the
      root of the UI in `MainActivity.kt`, using a reusable `LockScreenOrientation`
      composable ([1]).

## How to Run the Project

1. **Clone the repository:**

   ```powershell, cmd
       git clone https://github.com/diegoflassa/Fuse.CSGOmatches.git
       cd Fuse.CSGOMatches
   ```

2. **Open in Android Studio:**
    * Open Android Studio (latest stable version recommended).
    * Click on "Open" or "Open an Existing Project".
    * Navigate to the cloned `Fuse.CSGOMatches` directory and select it.
3. **Build the Project:**
    * Android Studio should automatically sync the Gradle project. If not, click on "Sync
      Project with Gradle Files" (elephant icon in the toolbar).
    * Once synced, build the project using "Build" > "Make Project" or by clicking the
      hammer icon.
4. **Run the Application:**
    * Select the `app` configuration from the run configurations dropdown.
    * Choose an emulator or connect a physical Android device (with USB debugging
      enabled).
    * Click the "Run" button (green play icon).

## Project Configuration Notes

* **API Key (`app\\config.xml`)**: This file contains the API key for the PandaScore
  service. Normally, this file would be added to `.gitignore` to prevent committing
  sensitive credentials. However, for the purpose of this challenge and for convenience,
  it has been committed to the repository. In a production environment, ensure such files
  are properly gitignored and keys are managed securely (e.g., via environment
  variables, CI secrets, etc.

---


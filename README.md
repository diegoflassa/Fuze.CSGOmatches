# Fuse CSGO Matches

*  Fuse CSGO Matches is an Android application that displays a list of Counter-Strike: Global
   Offensive (CS:GO) matches.

## Implemented Features (Brief)

This project showcases a range of modern Android development practices and features, including:

*   Displays a list of upcoming CS:GO matches.
*   Shows details for a selected match.
*   **Pagination Support**: Efficiently loads match data in pages for a smooth user experience.
*   **Reactive Programming**: Utilizes Kotlin Flows for managing asynchronous data streams and UI updates.
*   **Responsive UI**: The user interface is designed to adapt to various screen sizes and orientations.
*   **Comprehensive Unit Tests**: Thorough unit tests have been implemented to ensure code quality and correctness.
*   **Localization**: Supports both English (en) and Portuguese (pt) languages.
*   Some of the design decisions were made to showcase my knowledge in Android development,
    and, although not needed in such a simple project, I chose to implement them.
*   Added a filter to enable users to see upcoming or all matches.

## Suggestions for Improvement

*   Add a filter to enable to show all, or upcoming Matches (Implemented)
*   Add one badge for each status of the Match
    ( Supported statuses that include badges are: (`ENDED`), (`IN_PROGRESS`), and (`SCHEDULED`),
    but there are also (`FINISHED`), (`CANCELED`) and (`NOT_STARTED`) )
*   It could be allowed to use the compose's CircularProgressIndicator instead of the system.
    ( I used the system, through and AndroidView, in the method (`SystemCircularLoadingIndicator`) )

## Tech Stack & Architectural Decisions

*  This project is built using modern Android development practices and libraries.
*  Using Android Studio Narwhal | 2025.1.1 Patch 1, and Java 21
*  Minimum SDK Version 28

### Core Architecture

*   **build-config**: The project uses a build config module that is used to store all logic related
    to the build and, if needed share it.
    This makes the modules build.gradle.kts appear "naked", but all its logics is applied from
    (`android-application-convention.gradle.kts`) or (`android-library-convention.gradle.kts`)
*   **Modular Design**: The project is structured into multiple modules (`app`, `feature-core`,
    `feature-main`, `feature-details`) to promote separation of concerns, scalability, and
    maintainability.
*   **MVI (Model-View-Intent)**: This architectural pattern is used to structure the presentation
    layer, chosen over MVVM for its explicit unidirectional data flow and clear separation of concerns.
*   **Dependency Injection (Hilt)**: Uses Dagger Hilt for managing dependencies throughout the
    application, simplifying the setup and providing a standard way to perform DI.
*   **100% Kotlin**: The entire project is written in Kotlin.
*   **Dependency Management**: Uses Gradle Version Catalogs (`libs.versions.toml`) for
    centralized and easier management of dependency versions.

### UI

*   **Jetpack Compose**: The UI is built entirely with Jetpack Compose, Android's modern
    declarative UI toolkit.
    *   **Navigation (androidx.navigation3)**: Utilized for navigating between different screens,
        integrated with Jetpack Compose.
    *   **Material 3**: Implements Material Design 3 components and theming.
    *   **ConstraintLayout for Compose**: Utilized for more complex layouts where
        ConstraintLayout's capabilities are beneficial.
*   **Coil**: For efficient image loading in Compose.
*   **AndroidX Core Splashscreen**: Implements the modern Android splash screen.

### Networking & Data

*   **Retrofit 3**: For type-safe HTTP client communication with a backend API.
*   **OkHttp 3**: As the underlying HTTP client for Retrofit, including a logging
    interceptor for easier debugging.
*   **Kotlinx Serialization**: For efficient JSON parsing (serialization/deserialization).

### Asynchronous Programming

*   **Kotlin Coroutines & Flow**: Used extensively for managing background tasks, asynchronous
    operations, and reactive data streams, integrated with AndroidX Lifecycle components.

### Other Key Libraries

*   **AndroidX Lifecycle**: For lifecycle-aware components (ViewModels, LiveData observation
    with Compose).
*   **Unit & Instrumented Testing**:
    *   **JUnit 4/5**: For local unit tests.
    *   **MockK**: For creating mocks in unit tests.
    *   **Turbine**: For testing Kotlin Flows.
    *   **AndroidX Test (JUnit KTX, Espresso)**: For instrumented tests.

## Prerequisites

*   **Java Development Kit (JDK) 21**: This project requires JDK 21. Please ensure it is
    installed and configured in your system and Android Studio. You can download it from
    [Oracle's website](https://www.oracle.com/java/technologies/downloads/#java21)
    or use a distribution like OpenJDK.

## How to Run the Project

1.  **Clone the repository:**

    ```powershell, cmd
        git clone https://github.com/diegoflassa/Fuse.CSGOmatches.git
        cd Fuse.CSGOMatches
    ```

2.  **Open in Android Studio:**
    *   Open Android Studio (latest stable version recommended).
    *   Click on "Open" or "Open an Existing Project".
    *   Navigate to the cloned `Fuse.CSGOMatches` directory and select it.
3.  **Verify the if Java 21 is installed:** 
    *   If is not in the windows PATH, Android Studio will ask you to choose a proper JVM
    *   If is in the windows PATH, Android Studio open the project normally
4.  **Build the Project:**
    *   Android Studio should automatically sync the Gradle project. If not, click on "Sync
        Project with Gradle Files" (elephant icon in the toolbar).
    *   Once synced, build the project using "Build" > "Make Project" or by clicking the
        hammer icon.
5.  **Run the Application:**
    *   Select the `app` configuration from the run configurations dropdown.
    *   Choose an emulator or connect a physical Android device (with USB debugging
        enabled).
    *   Click the "Run" button (green play icon).

## Project Configuration Notes

*   **API Key (`app\config.xml`)**: This file contains the API key for the PandaScore
    service. Normally, this file would be added to `.gitignore` to prevent committing
    sensitive credentials. However, for the purpose of this challenge and for convenience,
    it has been added to the repository. In a production environment, ensure such files
    are properly gitignored and keys are managed securely (e.g., via environment
    variables, CI secrets, etc.

---

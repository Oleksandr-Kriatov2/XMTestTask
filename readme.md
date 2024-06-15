# XMTestTask

XMTestTask is an Android application built using Jetpack Compose, Kotlin, and the MVI architecture pattern. This app presents a series of survey questions to the user and allows them to submit their answers. It also demonstrates the use of various Android and Jetpack libraries, including Dagger-Hilt for dependency injection, Jetpack Navigation for in-app navigation, and Jetpack Compose for the UI.

## Features

- Display a list of survey questions.
- Navigate through questions using previous and next buttons.
- Submit answers to the server.
- Display submission results with a Snackbar.
- Show a loading indicator while fetching data.

## Technologies Used

- Kotlin
- Jetpack Compose
- Kotlin Coroutines
- Dagger-Hilt
- Jetpack Navigation
- Mockk
- Kotest
- Turbine

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/Oleksandr-Kriatov2/XMTestTask.git
    ```

2. Open the project in Android Studio.

3. Sync the project with Gradle.

4. Run the project on an emulator or physical device.

## Usage

### Navigating the App

- The app starts with a welcome screen.
- Click "Start survey" to navigate to the question screen.
- Use the previous and next buttons to navigate through the questions.
- Enter your answers and click "Submit" to submit your answers.
- The app will display a Snackbar with the submission result.

## Testing

The project uses Kotest and Mockk for unit testing, and Turbine for testing flows. To run the tests, use the following command in the terminal:

```sh
./gradlew test
```

Test Coverage
The tests cover various aspects of the SurveyViewModel, ensuring correct behavior for loading questions, navigating through questions, updating answers, and submitting answers.
To see test coverage report run following command in terminal:

```sh
./gradlew JacocoDebugCodeCoverage
```
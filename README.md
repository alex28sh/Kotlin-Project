# Kotlin-Project
Project for Development Ecosystem Course

Right now implemented [plugin](KotlinProject/gradle-plugins/description-plugin/src/main/kotlin/com/example/DescriptionPlugin.kt), [server](KotlinProject/server/src/main/kotlin/org/example/project/Application.kt)
and [desktopApp](KotlinProject/composeApp/src/commonMain/kotlin/org/example/project/App.kt). 

* Plugin can be run by command `./gradlew describe`.
* Desktop App can be run by `./gradlew :composeApp:run`.
* Server can be run by `./gradlew :server:run`.

Right now desktopApp creates only one window with the list of book titles. Server doesn't have any storage.
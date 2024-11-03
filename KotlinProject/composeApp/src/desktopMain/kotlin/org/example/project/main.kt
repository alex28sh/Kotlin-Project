package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Book Titles",
    ) {
        App(true)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Book Authors",
    ) {
        App(false)
    }
}
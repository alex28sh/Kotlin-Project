package org.example.project

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        val books = remember { mutableStateListOf<String>() }
        var currentOffset by remember { mutableStateOf(0) }
        val isLoading = remember { mutableStateOf(false) }
        val listState = rememberLazyListState()


        val isEndReached by remember {
            derivedStateOf {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val totalItems = listState.layoutInfo.totalItemsCount
                lastVisibleItemIndex != null && totalItems != null && lastVisibleItemIndex >= totalItems - 1
            }
        }

        LaunchedEffect(isEndReached) {
            if (isEndReached && !isLoading.value) {
                isLoading.value = true
                val newBooks = ServerInteraction().fetchBooksFromServer(limit = 20, offset = currentOffset)
                books.addAll(newBooks)
                currentOffset += 20
                isLoading.value = false
            }
        }

        LazyColumn (state = listState) {

            items(books) { book ->
                Text(book) // Your UI for each book item
            }

        }
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember<String> { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
    }
}
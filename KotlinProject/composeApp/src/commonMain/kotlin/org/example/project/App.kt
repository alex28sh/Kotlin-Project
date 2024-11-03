package org.example.project

import androidx.compose.foundation.layout.fillMaxWidth
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
@Preview
fun App(needTitles : Boolean) {
    MaterialTheme {
        val items = remember { mutableStateListOf<String>() }
        var currentOffset by remember { mutableStateOf(0) }
        val listState = rememberLazyListState()

        val isEndReached by remember {
            derivedStateOf {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val totalItems = listState.layoutInfo.totalItemsCount
                lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItems - 1
            }
        }

        LaunchedEffect(isEndReached) {
            snapshotFlow { isEndReached }
                .distinctUntilChanged()
                .collect {
                    val newItems = ServerInteraction().fetchItemsFromServer(limit = 20, offset = currentOffset, needTitles = needTitles)
                    items.addAll(newItems)
                    currentOffset += 20
                }
        }

        LazyColumn (state = listState, modifier = Modifier.fillMaxWidth()) {

            items(items) { item ->
                Text(item, fontSize = 40.sp) // Your UI for each book item
            }

        }
    }
}
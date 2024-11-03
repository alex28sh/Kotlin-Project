package org.example.project

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Book(
    val title: String,
    val authors: List<Author>
)

@Serializable
data class Author(
    val name: String
)

@Serializable
data class BookSearchResponse(
    val numFound: Int,
    val docs: List<BookDocument>,
)

@Serializable
data class BookDocument(
    val author_name: List<String>,
    val title: String
)

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0") {
        routing {
            get("/") {
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val needTitles = call.parameters["needTitles"]?.toBoolean() ?: true
                val books = fetchBooks(limit, offset)
                if (needTitles) {
                    call.respond(Json.encodeToString(books.map { it.title }))
                } else {
                    call.respond(Json.encodeToString(books.flatMap { it.authors }.map { it.name }))
                }
            }
        }
    }.start(wait = true)
}

suspend fun fetchBooks(limit: Int, offset: Int): List<Book> {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    val response: HttpResponse = client.get("http://openlibrary.org/search.json") {
        parameter("q", "fantasy")
        parameter("limit", limit)
        parameter("offset", offset)
    }

    val json = Json {
        ignoreUnknownKeys = true
    }

    val booksResponse = json.decodeFromString<BookSearchResponse>(response.bodyAsText())
    return booksResponse.docs.map { doc ->
        Book(title = doc.title, authors = doc.author_name?.map { Author(it) } ?: emptyList())
    }
}

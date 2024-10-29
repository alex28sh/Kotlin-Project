package org.example.project

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class ServerInteraction {

    suspend fun fetchBooksFromServer(limit: Int, offset: Int): List<String> {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val response: HttpResponse = client.get("http://0.0.0.0:8080") {
            parameter("limit", limit)
            parameter("offset", offset)
        }

        val json = Json {
            ignoreUnknownKeys = true
        }

        val booksResponse =
            json.decodeFromString<Pair<List<String>, List<String>>>(response.bodyAsText())

        return booksResponse.first
    }
}
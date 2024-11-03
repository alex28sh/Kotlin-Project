package org.example.project

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class ServerInteraction {

    suspend fun fetchItemsFromServer(limit: Int, offset: Int, needTitles: Boolean): List<String> {
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
            parameter("needTitles", needTitles)
        }

        val json = Json {
            ignoreUnknownKeys = true
        }

        return json.decodeFromString(response.bodyAsText())
    }
}
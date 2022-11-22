package dev.proxyfox.pluralkt.rest

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

object RestClient {
    const val baseUrl = "https://api.pluralkit.me/v2/"
    private val client = HttpClient(CIO) {
        install(UserAgent) {
            agent = "Plural.kt library"
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
}

fun <T> ArrayList<T>.push(t: T) = add(t)
fun <T> ArrayList<T>.pop(): T? = removeLastOrNull()
fun <T> ArrayList<T>.shift(): T? = removeFirstOrNull()
fun <T> ArrayList<T>.unshift(t: T) = add(0, t)

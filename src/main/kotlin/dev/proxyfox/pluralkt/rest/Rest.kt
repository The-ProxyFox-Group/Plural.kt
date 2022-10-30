package dev.proxyfox.pluralkt.rest

import dev.proxyfox.pluralkt.types.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*

object Rest {
    private const val baseUrl = "https://api.pluralkit.me/v2/"
    private val client = HttpClient(CIO) {
        install(UserAgent) {
            agent = "Plural.kt library"
        }
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun fetchSystem(systemRef: String, token: String? = null): ApiSystem? = get("systems/$systemRef/", token)

    

    private suspend inline fun <reified T> get(path: String, token: String?): T? {
        val res = client.get(baseUrl+path) {
            if (token != null) {
                header("Authorization", token)
            }
        }
        if (res.status.value == 200) return res.body()
        return null
    }
}
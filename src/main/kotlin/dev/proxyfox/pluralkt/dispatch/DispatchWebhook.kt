package dev.proxyfox.pluralkt.dispatch

import dev.proxyfox.pluralkt.dispatch.event.EventDispatcher
import dev.proxyfox.pluralkt.dispatch.types.PingEvent
import dev.proxyfox.pluralkt.dispatch.types.Event
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Job
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.decodeFromString

/**
 * Represents a PluralKit dispatch webhook.
 *
 * @param token The signing token for the webhook
 * @param id The unique ID for this webhook
 * */
class DispatchWebhook(val token: String, private val id: String = "") {
    val dispatcher = EventDispatcher<Event>()

    suspend inline fun <reified T : Event> on(noinline consumer: suspend T.() -> Unit): Job = dispatcher.on(consumer=consumer)

    fun start() {
        if (webhooks[id] != this) throw IllegalStateException("ID $id is taken by another webhook!")
        if (id in webhooks) throw IllegalStateException("ID $id is already active!")
        webhooks[id] = this
    }

    fun stop() {
        if (id !in webhooks) throw IllegalStateException("ID $id is not active!")
        if (webhooks[id] != this) throw IllegalStateException("ID $id is taken by another webhook!")
        webhooks.remove(id)
    }

    companion object {
        private val serializersModule = SerializersModule {
            polymorphic(Event::class) {
                subclass(PingEvent::class)
            }
        }
        val json = Json {
            ignoreUnknownKeys = true
            serializersModule = this@Companion.serializersModule
        }

        private val webhooks = hashMapOf<String, DispatchWebhook>()

        fun Routing.initDispatch(endpoint: String = "/") {
            post("$endpoint/{id}") {
                val webhook = webhooks[call.parameters["id"]] ?: return@post call.respond(404)
                val event = json.decodeFromString<Event>(call.receiveText())
                if (event.signingToken != webhook.token) return@post call.respond(401)
                webhook.dispatcher.emitEvent(event)
                call.respond(200)
            }
        }
    }
}



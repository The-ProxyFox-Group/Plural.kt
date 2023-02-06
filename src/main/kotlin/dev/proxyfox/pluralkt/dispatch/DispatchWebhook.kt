package dev.proxyfox.pluralkt.dispatch

import dev.proxyfox.pluralkt.dispatch.types.Event
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Job
import kotlinx.serialization.json.Json
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

    /**
     * Starts accepting events.
     *
     * Only works if using [initGlobalDispatch]
     * */
    fun start() {
        if (webhooks[id] != this) throw IllegalStateException("ID $id is taken by another webhook!")
        if (id in webhooks) throw IllegalStateException("ID $id is already active!")
        webhooks[id] = this
    }

    /**
     * Stops accepting events.
     *
     * Only works if using [initGlobalDispatch]
     * */
    fun stop() {
        if (id !in webhooks) throw IllegalStateException("ID $id is not active!")
        if (webhooks[id] != this) throw IllegalStateException("ID $id is taken by another webhook!")
        webhooks.remove(id)
    }

    /**
     * Sets up routing for this webhook only.
     * Useful for when you only need a single webhook
     *
     * Use [initGlobalDispatch] if you're setting up multiple webhooks
     * */
    fun Routing.initSingletonDispatch() {
        post {
            handleCall(this@DispatchWebhook, call)
        }
    }

    companion object {
        val json = Json {
            ignoreUnknownKeys = true
        }

        private val webhooks = hashMapOf<String, DispatchWebhook>()

        private suspend fun handleCall(webhook: DispatchWebhook, call: ApplicationCall) {
            try {
                val text = call.receiveText()
                val event = json.decodeFromString<Event>(text)
                event.jsonData = text
                if (event.signingToken != webhook.token) return call.respond(401)
                webhook.dispatcher.emitEvent(event)
                call.respond(200)
            } catch (err: Throwable) {
                err.printStackTrace()
                call.respond(500)
            }
        }

        /**
         * Sets up global dispatch
         *
         * Use [initSingletonDispatch] if you only need one webhook
         * */
        fun Routing.initGlobalDispatch(endpoint: String = "/") {
            route(endpoint) {
                post("{id}") {
                    val webhook = webhooks[call.parameters["id"]] ?: return@post call.respond(404)
                    handleCall(webhook, call)
                }
            }
        }
    }
}

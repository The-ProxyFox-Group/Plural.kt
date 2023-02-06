package dev.proxyfox.pluralkt.test.dispatch

import dev.proxyfox.pluralkt.dispatch.DispatchWebhook
import dev.proxyfox.pluralkt.dispatch.types.Event
import dev.proxyfox.pluralkt.dispatch.types.PingEvent
import dev.proxyfox.pluralkt.dispatch.types.SystemUpdateEvent
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*

val token = System.getenv("DISPATCH_TOKEN")

suspend fun main() {
    val webhook = DispatchWebhook(token)

    webhook.on<Event> {
        println("${this::class.simpleName}, $data")
    }
    embeddedServer(CIO, port = 8080) {
        routing {
            with(webhook) {
                initSingletonDispatch()
            }
        }
    }.start(wait = true)
}

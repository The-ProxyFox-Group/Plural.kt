package dev.proxyfox.pluralkt.test.dispatch

import dev.proxyfox.pluralkt.dispatch.DispatchWebhook
import dev.proxyfox.pluralkt.dispatch.types.PingEvent
import dev.proxyfox.pluralkt.dispatch.types.Event
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

fun main() {
    val test = PingEvent("", "", null)
    val json = DispatchWebhook.json.encodeToString(test)
    println(json)
    println(DispatchWebhook.json.decodeFromString<Event>("""
        {
            "type": "PING",
            "signing_token": "",
            "system_id": "",
            "id": null
        }
    """.trimIndent()))
}

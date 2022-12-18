package dev.proxyfox.pluralkt.test

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.json.*
import kotlinx.serialization.*
import kotlin.system.*

fun main() {
    PluralKt.getSystem("aaaaa") { // FAIL
        println("aaaaa")
        println(Json.encodeToString(this.getError()))
    }
    PluralKt.getSystem(System.getenv("TEST_PKSYS")) { // SUCCESS
        println(System.getenv("TEST_PKSYS"))
        println(Json.encodeToString(this.getSuccess()))
        exitProcess(0)
    }
}
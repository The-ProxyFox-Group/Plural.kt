package dev.proxyfox.pluralkt.test

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.json.*
import kotlinx.serialization.*
import kotlin.system.*

val json = Json {
    prettyPrint = true
}

fun main() {
    PluralKt.System.getSystem("aaaaa") { // FAIL
        println("GET: aaaaa")
        println("Success: "+this.isSuccess())
        println("Data: "+json.encodeToString(this.getError()))
    }
    PluralKt.System.getSystem(System.getenv("TEST_PKSYS")) { // SUCCESS
        println("GET: "+System.getenv("TEST_PKSYS"))
        println("Success: "+this.isSuccess())
        println("Data: "+json.encodeToString(this.getSuccess()))
    }
}
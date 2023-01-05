package dev.proxyfox.pluralkt.test

import dev.proxyfox.pluralkt.*
import dev.proxyfox.pluralkt.types.PkSystem
import kotlinx.serialization.json.*
import kotlinx.serialization.*
import java.util.UUID

val json = Json {
    prettyPrint = true
}

fun main() {
    PluralKt.System.getSystem("aaaaa") { // FAIL
        println("GET: aaaaa")
        println("Success: "+this.isSuccess())
        println("Data: "+toString())
    }
    PluralKt.System.getSystem("exmpl") { // SUCCESS
        println("GET: exmpl")
        println("Success: "+this.isSuccess())
        println("Data: "+toString())
    }
}
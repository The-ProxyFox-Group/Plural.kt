package dev.proxyfox.pluralkt.test

import dev.proxyfox.pluralkt.*
import kotlinx.coroutines.delay

suspend fun main() {
    PluralKt.System.getSystem("aaaaa") {
        println("GET: aaaaa")
        println("Success: "+this.isSuccess())
        println("Data: "+toString())
    }
    delay(10000L)
    PluralKt.System.getSystem("exmpl") {
        println("GET: exmpl")
        println("Success: "+this.isSuccess())
        println("Data: "+toString())
    }
}
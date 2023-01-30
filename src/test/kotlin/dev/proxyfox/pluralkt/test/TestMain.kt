package dev.proxyfox.pluralkt.test

import dev.proxyfox.pluralkt.*
import dev.proxyfox.pluralkt.types.PkMessage
import dev.proxyfox.pluralkt.types.PkSnowflake

fun main() {
    PluralKt.System.getSystem("aaaaa").onComplete {
        println("GET: aaaaa")
        println("Success: "+this.isSuccess())
        println("Data: "+toString())
    }
    val system = PluralKt.System.getSystem("exmpl").await()
    println("GET: exmpl")
    println("Success: "+system.isSuccess())
    println("Data: $system")
}
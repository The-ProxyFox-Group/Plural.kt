package dev.proxyfox.pluralkt.test

import dev.proxyfox.pluralkt.*

fun main() {
    PluralKt.System.getSystem("aaaaa") {
        println("GET: aaaaa")
        println("Success: "+this.isSuccess())
        println("Data: "+toString())
    }
    PluralKt.System.getSystem("exmpl") {
        println("GET: exmpl")
        println("Success: "+this.isSuccess())
        println("Data: "+toString())
    }
}
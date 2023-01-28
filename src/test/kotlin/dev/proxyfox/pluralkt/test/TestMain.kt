package dev.proxyfox.pluralkt.test

import dev.proxyfox.pluralkt.*
import dev.proxyfox.pluralkt.types.PkMessage
import dev.proxyfox.pluralkt.types.PkSnowflake

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
    PluralKt.Misc.getMessage(PkSnowflake(1068939961946083348UL)) {
        println("GET: 1068939961946083348UL")
        println("Success: "+this.isSuccess())
        println("Data: "+toString())
    }
}
package dev.proxyfox.pluralkt.test

import dev.proxyfox.pluralkt.*
import dev.proxyfox.pluralkt.types.ApiSystem
import dev.proxyfox.pluralkt.types.PkColor
import kotlinx.datetime.Clock
import kotlinx.serialization.json.*
import kotlinx.serialization.*
import java.util.*
import kotlin.system.*

val json = Json {
    prettyPrint = true
}

fun main() {
    PluralKt.System.getSystem("aaaaa") { // FAIL
        println("GET: aaaaa")
        println("Success: "+this.isSuccess())
        if (this.isSuccess())
            println("Data: "+json.encodeToString(this.getSuccess()))
        else if (this.isError())
            println("Data: "+json.encodeToString(this.getError()))
        else println("Data: Null")
    }
    PluralKt.System.getSystem("exmpl") { // SUCCESS
        println("GET: exmpl")
        println("Success: "+this.isSuccess())
        if (this.isSuccess())
            println("Data: "+json.encodeToString(this.getSuccess()))
        else if (this.isError())
            println("Data: "+json.encodeToString(this.getError()))
        else println("Data: Null")
    }

    println(Json.encodeToString(ApiSystem()))
}
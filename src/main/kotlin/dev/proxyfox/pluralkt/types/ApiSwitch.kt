package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*

@Serializable
class ApiSwitch : ApiType {
    val id: PkUuid = ""
    var timestamp: Instant = Clock.System.now()
    var members: ArrayList<String> = arrayListOf()

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

@Serializable
class ApiFronter : ApiType {
    val id: PkUuid = ""
    var timestamp: Instant = Clock.System.now()
    var members: ArrayList<ApiMember> = arrayListOf()

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

class SwitchCreate(vararg members: PkReference) : ApiType {
    val timestamp: Instant = Clock.System.now()
    var members: ArrayList<PkReference> = arrayListOf()
    init {
        this.members = arrayListOf(*members)
    }

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class ApiSwitch : ApiType {
    val id: PkUuid = ""
    var timestamp: PkTimestamp = ""
    var members: ArrayList<String> = arrayListOf()

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

@Serializable
class ApiFronter : ApiType {
    val id: PkUuid = ""
    var timestamp: PkTimestamp = ""
    var members: ArrayList<ApiMember> = arrayListOf()

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

class SwitchCreate(vararg members: PkReference) : ApiType {
    val timestamp: PkTimestamp? = null
    var members: ArrayList<PkReference> = arrayListOf()
    init {
        this.members = arrayListOf(*members)
    }

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

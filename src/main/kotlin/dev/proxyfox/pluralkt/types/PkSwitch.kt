package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*

@Serializable
class PkSwitch : PkType {
    val id: PkUuid = ""
    var timestamp: Instant = Clock.System.now()
    var members: ArrayList<String> = arrayListOf()

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

@Serializable
class PkFronter : PkType {
    val id: PkUuid = ""
    var timestamp: Instant = Clock.System.now()
    var members: ArrayList<PkMember> = arrayListOf()

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

class SwitchCreate(vararg members: PkReference) : PkType {
    val timestamp: Instant = Clock.System.now()
    var members: ArrayList<PkReference> = arrayListOf()
    init {
        this.members = arrayListOf(*members)
    }

    override fun toString(): String = PluralKt.json.encodeToString(this)
}

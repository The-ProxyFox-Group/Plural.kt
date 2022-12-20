package dev.proxyfox.pluralkt.types

import kotlinx.serialization.Serializable

@Serializable
class ApiSwitch : ApiType {
    val id: PkUuid = ""
    var timestamp: PkTimestamp = ""
    var members: ArrayList<String> = arrayListOf()
}

@Serializable
class ApiFronter : ApiType {
    val id: PkUuid = ""
    var timestamp: PkTimestamp = ""
    var members: ArrayList<ApiMember> = arrayListOf()
}

class SwitchCreate(vararg members: PkReference) : ApiType {
    val timestamp: PkTimestamp? = null
    var members: ArrayList<PkReference> = arrayListOf()
    init {
        this.members = arrayListOf(*members)
    }
}

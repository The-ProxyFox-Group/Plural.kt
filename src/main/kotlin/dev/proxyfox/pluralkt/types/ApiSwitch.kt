package dev.proxyfox.pluralkt.types

import kotlinx.serialization.Serializable

@Serializable
class ApiSwitch : ApiType {
    val id: PkUuid = ""
    val timestamp: PkTimestamp = ""
    val members: ArrayList<String> = arrayListOf()
}

@Serializable
class ApiFronter : ApiType {
    val id: PkUuid = ""
    val timestamp: PkTimestamp = ""
    val members: ArrayList<ApiMember> = arrayListOf()
}
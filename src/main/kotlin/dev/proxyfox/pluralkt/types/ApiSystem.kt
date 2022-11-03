package dev.proxyfox.pluralkt.types

class ApiSystem {
    val id: PkId = ""
    val uuid: PkUuid = ""
    val created: PkTimestamp = ""
    var name: String? = null
    var description: String? = null
    var tag: String? = null
    var pronouns: String? = null
    var avatar_url: String? = null
    var banner: String? = null
    var color: PkColor = ""
    var privacy: SystemPrivacy? = null
}
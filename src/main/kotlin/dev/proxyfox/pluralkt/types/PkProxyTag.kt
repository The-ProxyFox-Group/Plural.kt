package dev.proxyfox.pluralkt.types

import dev.proxyfox.pluralkt.*
import kotlinx.serialization.*

@Serializable
class PkProxyTag : PkType {
    var prefix: String? = null
    var suffix: String? = null

    override fun toString(): String = PluralKt.json.encodeToString(this)
}
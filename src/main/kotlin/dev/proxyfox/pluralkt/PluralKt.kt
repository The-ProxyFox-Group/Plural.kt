package dev.proxyfox.pluralkt

import dev.proxyfox.pluralkt.types.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.datetime.Instant
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import java.util.concurrent.*

object PluralKt {
    private val requestQueue: ArrayList<Request<*, *>> = arrayListOf()
    private const val baseUrl = "https://api.pluralkit.me/v2/"
    private val module = SerializersModule {
        polymorphic(PkType::class) {
            subclass(PkErrorMessage::class)
            subclass(PkAutoProxy::class)
            subclass(PkColor::class)
            subclass(PkError::class)
            subclass(PkGroup::class)
            subclass(PkGuildMember::class)
            subclass(PkGuildSystem::class)
            subclass(PkMember::class)
            subclass(PkMemberPrivacy::class)
            subclass(PkMessage::class)
            subclass(PkProxyTag::class)
            subclass(PkSwitch::class)
            subclass(PkFronter::class)
            subclass(PkSystem::class)
            subclass(PkSystemPrivacy::class)
            subclass(PkSystemSettings::class)
        }
    }
    val json = Json {
        serializersModule = module
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
    private val client = HttpClient(CIO) {
        install(UserAgent) {
            agent = "Plural.kt library"
        }
        install(ContentNegotiation) {
            json(json)
        }
    }
    private var scheduler = Executors.newScheduledThreadPool(1)
    private var tmpDelay: Long = 0
    private var active: Boolean = false

    private suspend fun <I, O> completeRequest(request: Request<I, O>): HttpResponse {
        val req = when (request.type) {
            RequestType.GET -> client.get(request.createUrl(baseUrl)) {
                request.token?.let { header("Authorization", it) }
            }
            RequestType.POST -> client.post(request.createUrl(baseUrl)) {
                header("content-type", "application/json")
                request.token?.let { header("Authorization", it) }
                setBody(request.data, request.inputTypeInfo)
            }
            RequestType.PATCH -> client.patch(request.createUrl(baseUrl)) {
                header("content-type", "application/json")
                request.token?.let { header("Authorization", it) }
                setBody(request.data, request.inputTypeInfo)
            }
            RequestType.DELETE -> client.delete(request.createUrl(baseUrl)) {
                request.token?.let { header("Authorization", it) }
            }
        }
        if (req.status.value == 200) {
            try {
                request.onComplete(ResponseSuccess(req.body(request.outputTypeInfo)))
            } catch (a: JsonConvertException) {
                try {
                    request.onComplete(ResponseError(req.body(), a))
                } catch (b: JsonConvertException) {
                    b.addSuppressed(a)
                    request.onComplete(ResponseNull(b))
                }
            }
        } else {
            try {
                request.onComplete(ResponseError(req.body()))
            } catch (b: JsonConvertException) {
                request.onComplete(ResponseNull(b))
            }
        }

        return req
    }

    private fun schedule(delay: Long) {
        if (requestQueue.isEmpty()) {
            tmpDelay = delay
            active = false
            scheduler.shutdown()
            return
        }
        scheduler.schedule({
            runBlocking {
                try {
                    handleRequest()
                } catch (err: Throwable) {
                    err.printStackTrace()
                    schedule(0)
                }
            }
        }, delay, TimeUnit.MILLISECONDS)
    }

    private suspend fun handleRequest() {
        val request = requestQueue.shift() ?: run {
            schedule(0)
            return
        }
        val res = completeRequest(request)
        schedule(1000/res.headers["x-ratelimit-limit"]!!.toLong())
    }

    fun <I, O> push(request: Request<I, O>): Request<I, O> {
        requestQueue.push(request)
        if (!active) {
            active = true
            scheduler = Executors.newScheduledThreadPool(1)
            schedule(tmpDelay)
        }
        return request
    }

    object System {
        fun getMe(token: String): Request<PkType, PkSystem> {
            return push(get("systems/@me", token))
        }

        fun getSystem(systemRef: PkReference, token: String? = null): Request<PkType, PkSystem> {
            return push(get("systems/$systemRef", token))
        }

        fun updateSystem(system: PkSystem, token: String): Request<PkSystem, PkSystem> {
            return push(patch("systems/@me", system, token))
        }

        fun getSystemSettings(systemRef: PkReference, token: String? = null): Request<PkType, PkSystemSettings> {
            return push(get("systems/$systemRef/settings", token))
        }

        fun updateSystemSettings(settings: PkSystemSettings, token: String): Request<PkSystemSettings, PkSystemSettings> {
            return push(patch("systems/@me/settings", settings, token))
        }

        fun getSystemGuildSettings(guild: PkSnowflake, token: String): Request<PkType, PkGuildSystem> {
            return push(get("systems/@me/guilds/$guild", token))
        }

        fun updateSystemGuildSettings(guild: PkSnowflake, settings: PkGuildSystem, token: String): Request<PkGuildSystem, PkGuildSystem> {
            return push(patch("systems/@me/guilds/$guild", settings, token))
        }

        fun getAutoProxy(guild: PkSnowflake, token: String): Request<PkType, PkAutoProxy> {
            return push(get("systems/@me/autoproxy?guild_id=$guild", token))
        }

        fun updateAutoProxy(guild: PkSnowflake, autoProxy: PkAutoProxy, token: String): Request<PkAutoProxy, PkAutoProxy> {
            return push(patch("systems/@me/autoproxy?guild_id=$guild", autoProxy, token))
        }
    }

    object Member {
        fun getMembers(systemRef: PkReference, token: String? = null): Request<PkType, Array<PkMember>> {
            return push(get("systems/$systemRef/members", token))
        }

        fun createMember(member: PkMember, token: String): Request<PkMember, PkMember> {
            return push(post("members", member, token))
        }

        fun getMember(memberRef: PkReference, token: String? = null): Request<PkType, PkMember> {
            return push(get("members/$memberRef", token))
        }

        fun updateMember(memberRef: PkReference, member: PkMember, token: String): Request<PkMember, PkMember> {
            return push(patch("members/$memberRef", member, token))
        }

        fun deleteMember(memberRef: PkReference, token: String): Request<PkType, PkMember> {
            return push(delete("members/$memberRef", token))
        }

        fun getMemberGroups(memberRef: PkReference, token: String? = null): Request<PkType, Array<PkReference>> {
            return push(get("members/$memberRef/groups", token))
        }

        fun addMemberToGroups(memberRef: PkReference, groups: Array<PkReference>, token: String): Request<Array<PkReference>, Array<PkReference>> {
            return push(post("members/$memberRef/groups/add", groups, token))
        }

        fun removeMemberFromGroups(memberRef: PkReference, groups: Array<PkReference>, token: String): Request<Array<PkReference>, Array<PkReference>> {
            return push(post("members/$memberRef/groups/add", groups, token))
        }

        fun overwriteMemberGroups(memberRef: PkReference, groups: Array<PkReference>, token: String): Request<Array<PkReference>, Array<PkReference>> {
            return push(post("members/$memberRef/groups/overwrite", groups, token))
        }

        fun getMemberGuild(memberRef: PkReference, guild: PkSnowflake, token: String? = null): Request<PkType, PkMember> {
            return push(get("members/$memberRef/guilds/$guild", token))
        }

        fun updateMemberGuild(memberRef: PkReference, guild: PkSnowflake, member: PkGuildMember, token: String): Request<PkGuildMember, PkGuildMember> {
            return push(patch("members/$memberRef/guilds/$guild", member, token))
        }
    }

    object Group {
        fun getGroups(systemRef: PkReference, token: String? = null): Request<PkType, Array<PkGroup>> {
            return push(get("systems/$systemRef/groups", token))
        }

        fun createGroup(group: PkGroup, token: String): Request<PkGroup, PkGroup> {
            return push(post("groups", group, token))
        }

        fun getGroup(groupRef: PkReference, token: String? = null): Request<PkType, PkGroup> {
            return push(get("groups/$groupRef", token))
        }

        fun updateGroup(groupRef: PkReference, group: PkGroup, token: String): Request<PkGroup, PkGroup> {
            return push(patch("groups/$groupRef", group, token))
        }

        fun deleteGroup(groupRef: PkReference, token: String): Request<PkType, PkGroup> {
            return push(delete("groups/$groupRef", token))
        }

        fun getGroupMembers(groupRef: PkReference, token: String? = null): Request<PkType, Array<PkMember>> {
            return push(get("groups/$groupRef/members", token))
        }

        fun addMembersToGroup(groupRef: PkReference, members: Array<PkReference>, token: String): Request<Array<PkReference>, Array<PkReference>> {
            return push(post("groups/$groupRef/members/add", members, token))
        }

        fun removeMembersFromGroup(groupRef: PkReference, members: Array<PkReference>, token: String): Request<Array<PkReference>, Array<PkReference>> {
            return push(post("groups/$groupRef/members/remove", members, token))
        }

        fun overwriteMembersInGroup(groupRef: PkReference, members: Array<PkReference>, token: String): Request<Array<PkReference>, Array<PkReference>> {
            return push(post("groups/$groupRef/members/overwrite", members, token))
        }
    }

    object Switch {
        fun getSwitches(systemRef: PkReference, before: Instant, limit: Int = 100, token: String? = null): Request<PkType, Array<PkSwitch>> {
            if (limit > 100) throw IllegalArgumentException("Limit cannot be greater than 100.")
            return push(get("systems/$systemRef/switches?$before&$limit", token))
        }

        fun getFronters(systemRef: PkReference, token: String? = null): Request<PkType, PkFronter> {
            return push(get("systems/$systemRef/fronters", token))
        }

        fun createSwitch(create: SwitchCreate, token: String): Request<SwitchCreate, PkFronter> {
            return push(post("systems/@me/switches", create, token))
        }

        fun getSwitch(systemRef: PkReference, switchRef: PkReference, token: String? = null): Request<PkType, PkFronter> {
            return push(get("systems/$systemRef/switches/$switchRef", token))
        }

        fun updateSwitch(systemRef: PkReference, switchRef: PkReference, switch: SwitchCreate, token: String): Request<SwitchCreate, PkFronter> {
            return push(patch("systems/$systemRef/switches/$switchRef", switch, token))
        }

        fun deleteSwitch(systemRef: PkReference, switchRef: PkReference, token: String): Request<PkType, PkFronter> {
            return push(delete("systems/$systemRef/switches/$switchRef", token))
        }

        fun updateSwitchMembers(systemRef: PkReference, switchRef: PkReference, members: Array<PkReference>, token: String): Request<Array<PkReference>, PkFronter> {
            return push(patch("systems/$systemRef/switches/$switchRef/members", members, token))
        }
    }

    object Misc {
        fun getMessage(message: PkSnowflake, token: String? = null): Request<PkType, PkMessage> {
            return push(get("messages/$message", token))
        }
    }
}

fun <T> ArrayList<T>.push(t: T) = add(t)
fun <T> ArrayList<T>.shift(): T? = removeFirstOrNull()

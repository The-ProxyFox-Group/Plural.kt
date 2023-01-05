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
            subclass(ErrorObject::class)
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
    }
    private val client = HttpClient(CIO) {
        install(UserAgent) {
            agent = "Plural.kt library"
        }
        install(ContentNegotiation) {
            json(json)
        }
    }
    private val scheduler = Executors.newScheduledThreadPool(1)

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
        try {
            request.onComplete(ResponseSuccess(req.body(request.outputTypeInfo)))
        } catch (_: JsonConvertException) {
            try {
                request.onComplete(ResponseError(req.body()))
            } catch (_: JsonConvertException) {
                request.onComplete(ResponseNull())
            }
        }
        return req
    }

    private fun schedule(delay: Long) {
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

    init {
        // TODO: Make this exit when main function exits
        schedule(0)
    }

    object System {
        fun getSystem(systemRef: PkReference, token: String? = null, onComplete: Response<PkSystem>.() -> Unit) {
            requestQueue.push(get("systems/$systemRef", null, onComplete))
        }

        fun updateSystem(system: PkSystem, token: String, onComplete: Response<PkSystem>.() -> Unit) {
            requestQueue.push(patch("systems/@me", system, token, onComplete))
        }

        fun getSystemSettings(systemRef: PkReference, token: String? = null, onComplete: Response<PkSystemSettings>.() -> Unit) {
            requestQueue.push(get("systems/$systemRef/settings", token, onComplete))
        }

        fun updateSystemSettings(settings: PkSystemSettings, token: String, onComplete: Response<PkSystemSettings>.() -> Unit) {
            requestQueue.push(patch("systems/@me/settings", settings, token, onComplete))
        }

        fun getSystemGuildSettings(guild: PkSnowflake, token: String, onComplete: Response<PkGuildSystem>.() -> Unit) {
            requestQueue.push(get("systems/@me/guilds/$guild", token, onComplete))
        }

        fun updateSystemGuildSettings(guild: PkSnowflake, settings: PkGuildSystem, token: String, onComplete: Response<PkGuildSystem>.() -> Unit) {
            requestQueue.push(patch("systems/@me/guilds/$guild", settings, token, onComplete))
        }

        fun getAutoProxy(guild: PkSnowflake, token: String, onComplete: Response<PkAutoProxy>.() -> Unit) {
            requestQueue.push(get("systems/@me/autoproxy?guild_id=$guild", token, onComplete))
        }

        fun updateAutoProxy(guild: PkSnowflake, autoProxy: PkAutoProxy, token: String, onComplete: Response<PkAutoProxy>.() -> Unit) {
            requestQueue.push(patch("systems/@me/autoproxy?guild_id=$guild", autoProxy, token, onComplete))
        }
    }

    object Member {
        fun getMembers(systemRef: PkReference, token: String? = null, onComplete: Response<Array<PkMember>>.() -> Unit) {
            requestQueue.push(get("systems/$systemRef/members", token, onComplete))
        }

        fun createMember(member: PkMember, token: String, onComplete: Response<PkMember>.() -> Unit) {
            requestQueue.push(post("members", member, token, onComplete))
        }

        fun getMember(memberRef: PkReference, token: String? = null, onComplete: Response<PkMember>.() -> Unit) {
            requestQueue.push(get("members/$memberRef", token, onComplete))
        }

        fun updateMember(memberRef: PkReference, member: PkMember, token: String, onComplete: Response<PkMember>.() -> Unit) {
            requestQueue.push(patch("members/$memberRef", member, token, onComplete))
        }

        fun deleteMember(memberRef: PkReference, token: String, onComplete: Response<PkMember>.() -> Unit) {
            requestQueue.push(delete("members/$memberRef", token, onComplete))
        }

        fun getMemberGroups(memberRef: PkReference, token: String? = null, onComplete: Response<Array<PkGroup>>.() -> Unit) {
            requestQueue.push(get("members/$memberRef/groups", token, onComplete))
        }

        fun addMemberToGroups(memberRef: PkReference, groups: Array<PkReference>, token: String, onComplete: Response<Array<PkReference>>.() -> Unit) {
            requestQueue.push(post("members/$memberRef/groups/add", groups, token, onComplete))
        }

        fun removeMemberFromGroups(memberRef: PkReference, groups: Array<PkReference>, token: String, onComplete: Response<Array<PkReference>>.() -> Unit) {
            requestQueue.push(post("members/$memberRef/groups/add", groups, token, onComplete))
        }

        fun overwriteMemberGroups(memberRef: PkReference, groups: Array<PkReference>, token: String, onComplete: Response<Array<PkReference>>.() -> Unit) {
            requestQueue.push(post("members/$memberRef/groups/overwrite", groups, token, onComplete))
        }

        fun getMemberGuild(memberRef: PkReference, guild: PkSnowflake, token: String? = null, onComplete: Response<PkMember>.() -> Unit) {
            requestQueue.push(get("members/$memberRef/guilds/$guild", token, onComplete))
        }

        fun updateMemberGuild(memberRef: PkReference, guild: PkSnowflake, member: PkGuildMember, token: String, onComplete: Response<PkGuildMember>.() -> Unit) {
            requestQueue.push(patch("members/$memberRef/guilds/$guild", member, token, onComplete))
        }
    }

    object Group {
        fun getGroups(systemRef: PkReference, token: String? = null, onComplete: Response<Array<PkGroup>>.() -> Unit) {
            requestQueue.push(get("systems/$systemRef/groups", token, onComplete))
        }

        fun createGroup(group: PkGroup, token: String, onComplete: Response<PkGroup>.() -> Unit) {
            requestQueue.push(post("groups", group, token, onComplete))
        }

        fun getGroup(groupRef: PkReference, token: String? = null, onComplete: Response<PkGroup>.() -> Unit) {
            requestQueue.push(get("groups/$groupRef", token, onComplete))
        }

        fun updateGroup(groupRef: PkReference, group: PkGroup, token: String, onComplete: Response<PkGroup>.() -> Unit) {
            requestQueue.push(patch("groups/$groupRef", group, token, onComplete))
        }

        fun deleteGroup(groupRef: PkReference, token: String, onComplete: Response<PkGroup>.() -> Unit) {
            requestQueue.push(delete("groups/$groupRef", token, onComplete))
        }

        fun getGroupMembers(groupRef: PkReference, token: String? = null, onComplete: Response<Array<PkMember>>.() -> Unit) {
            requestQueue.push(get("groups/$groupRef/members", token, onComplete))
        }

        fun addMembersToGroup(groupRef: PkReference, members: Array<PkReference>, token: String, onComplete: Response<Array<PkReference>>.() -> Unit) {
            requestQueue.push(post("groups/$groupRef/members/add", members, token, onComplete))
        }

        fun removeMembersFromGroup(groupRef: PkReference, members: Array<PkReference>, token: String, onComplete: Response<Array<PkReference>>.() -> Unit) {
            requestQueue.push(post("groups/$groupRef/members/remove", members, token, onComplete))
        }

        fun overwriteMembersInGroup(groupRef: PkReference, members: Array<PkReference>, token: String, onComplete: Response<Array<PkReference>>.() -> Unit) {
            requestQueue.push(post("groups/$groupRef/members/overwrite", members, token, onComplete))
        }
    }

    object Switch {
        fun getSwitches(systemRef: PkReference, before: Instant, limit: Int = 100, token: String? = null, onComplete: Response<Array<PkSwitch>>.() -> Unit) {
            if (limit > 100) throw IllegalArgumentException("Limit cannot be greater than 100.")
            requestQueue.push(get("systems/$systemRef/switches?$before&$limit", token, onComplete))
        }

        fun getFronters(systemRef: PkReference, token: String? = null, onComplete: Response<PkFronter>.() -> Unit) {
            requestQueue.push(get("systems/$systemRef/fronters", token, onComplete))
        }

        fun createSwitch(create: SwitchCreate, token: String, onComplete: Response<PkFronter>.() -> Unit) {
            requestQueue.push(post("systems/@me/switches", create, token, onComplete))
        }

        fun getSwitch(systemRef: PkReference, switchRef: PkReference, token: String? = null, onComplete: Response<PkFronter>.() -> Unit) {
            requestQueue.push(get("systems/$systemRef/switches/$switchRef", token, onComplete))
        }

        fun updateSwitch(systemRef: PkReference, switchRef: PkReference, switch: SwitchCreate, token: String, onComplete: Response<PkFronter>.() -> Unit) {
            requestQueue.push(patch("systems/$systemRef/switches/$switchRef", switch, token, onComplete))
        }

        fun deleteSwitch(systemRef: PkReference, switchRef: PkReference, token: String, onComplete: Response<PkFronter>.() -> Unit) {
            requestQueue.push(delete("systems/$systemRef/switches/$switchRef", token, onComplete))
        }

        fun updateSwitchMembers(systemRef: PkReference, switchRef: PkReference, members: Array<PkReference>, token: String, onComplete: Response<PkFronter>.() -> Unit) {
            requestQueue.push(patch("systems/$systemRef/switches/$switchRef/members", members, token, onComplete))
        }
    }

    object Misc {
        fun getMessage(message: PkSnowflake, token: String? = null, onComplete: Response<PkMessage>.() -> Unit) {
            requestQueue.push(get("messages/$message", token, onComplete))
        }
    }
}

fun <T> ArrayList<T>.push(t: T) = add(t)
fun <T> ArrayList<T>.shift(): T? = removeFirstOrNull()

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
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import java.util.concurrent.*

object PluralKt {
    private val requestQueue: ArrayList<Request<*>> = arrayListOf()
    private const val baseUrl = "https://api.pluralkit.me/v2/"
    private val module = SerializersModule {
        polymorphic(ApiType::class) {
            subclass(ApiSystemSettings::class)
            subclass(ApiSystem::class)
            subclass(ApiSwitch::class)
            subclass(ApiFronter::class)
            subclass(ApiProxyTag::class)
            subclass(ApiMessage::class)
            subclass(ApiMember::class)
            subclass(ApiGuildSystem::class)
            subclass(ApiGuildMember::class)
            subclass(ApiGroup::class)
            subclass(ApiError::class)
            subclass(ApiAutoProxy::class)
        }
    }
    private val json = Json {
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
    private val scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors())

    fun getSystem(systemRef: PkReference, token: String? = null, onComplete: Response<ApiSystem>.() -> Unit) {
        requestQueue.push(get("systems/$systemRef", null, onComplete))
    }

    fun updateSystem(systemRef: PkReference, system: ApiSystem, token: String, onComplete: Response<ApiSystem>.() -> Unit) {
        requestQueue.push(patch("systems/$systemRef", system, token, onComplete))
    }

    fun getSystemSettings(systemRef: PkReference, token: String? = null, onComplete: Response<ApiSystemSettings>.() -> Unit) {
        requestQueue.push(get("systems/$systemRef/settings", token, onComplete))
    }

    fun updateSystemSettings(systemRef: PkReference, settings: ApiSystemSettings, token: String, onComplete: Response<ApiSystemSettings>.() -> Unit) {
        requestQueue.push(patch("systems/$systemRef/settings", settings, token, onComplete))
    }

    fun getSystemGuildSettings(guild: PkSnowflake, token: String, onComplete: Response<ApiGuildSystem>.() -> Unit) {
        requestQueue.push(get("systems/@me/guilds/$guild", token, onComplete))
    }

    fun updateSystemGuildSettings(guild: PkSnowflake, settings: ApiGuildSystem, token: String, onComplete: Response<ApiGuildSystem>.() -> Unit) {
        requestQueue.push(patch("systems/@me/guilds/$guild", settings, token, onComplete))
    }

    fun getAutoProxy(guild: PkSnowflake, token: String, onComplete: Response<ApiAutoProxy>.() -> Unit) {
        requestQueue.push(get("systems/@me/autoproxy?guild_id=$guild", token, onComplete))
    }

    fun updateAutoProxy(guild: PkSnowflake, autoProxy: ApiAutoProxy, token: String, onComplete: Response<ApiAutoProxy>.() -> Unit) {
        requestQueue.push(patch("systems/@me/autoproxy?guild_id=$guild", autoProxy, token, onComplete))
    }

    fun getMembers(systemRef: PkReference, token: String?, onComplete: Response<Array<ApiMember>>.() -> Unit) {
        requestQueue.push(get("systems/$systemRef/members", token, onComplete))
    }

    fun createMember(member: ApiMember, token: String, onComplete: Response<ApiMember>.() -> Unit) {
        requestQueue.push(post("members", member, token, onComplete))
    }

    fun getMember(memberRef: PkReference, token: String?, onComplete: Response<ApiMember>.() -> Unit) {
        requestQueue.push(get("members/$memberRef", token, onComplete))
    }

    fun updateMember(memberRef: PkReference, member: ApiMember, token: String, onComplete: Response<ApiMember>.() -> Unit) {
        requestQueue.push(patch("members/$memberRef", member, token, onComplete))
    }

    fun deleteMember(memberRef: PkReference, token: String, onComplete: Response<ApiMember>.() -> Unit) {
        requestQueue.push(delete("members/$memberRef", token, onComplete))
    }

    private suspend fun <T> completeRequest(request: Request<T>): HttpResponse {
        val req = when (request.type) {
            RequestType.GET -> client.get(request.createUrl(baseUrl)) {
                request.token?.let { header("Authorization", it) }
            }
            RequestType.POST -> client.post(request.createUrl(baseUrl)) {
                header("content-type", "application/json")
                request.token?.let { header("Authorization", it) }
                setBody(request.data, request.typeInfo)
            }
            RequestType.PATCH -> client.patch(request.createUrl(baseUrl)) {
                header("content-type", "application/json")
                request.token?.let { header("Authorization", it) }
                setBody(request.data, request.typeInfo)
            }
            RequestType.DELETE -> client.delete(request.createUrl(baseUrl)) {
                request.token?.let { header("Authorization", it) }
            }
        }
        try {
            request.onComplete(ResponseSuccess(req.body(request.typeInfo)))
        } catch (_: JsonConvertException) {
            request.onComplete(ResponseError(req.body()))
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
            schedule(100)
            return
        }
        val res = completeRequest(request)
        schedule(1000/res.headers["x-ratelimit-limit"]!!.toLong())
    }

    init {
        schedule(0)
    }
}

fun <T> ArrayList<T>.push(t: T) = add(t)
fun <T> ArrayList<T>.shift(): T? = removeFirstOrNull()

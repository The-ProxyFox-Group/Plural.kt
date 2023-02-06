package dev.proxyfox.pluralkt.dispatch

import io.ktor.util.logging.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

class EventDispatcher<E> : CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default

    val events = MutableSharedFlow<E>()

    suspend inline fun <reified T : E> on(scope: CoroutineScope = this, noinline consumer: suspend T.() -> Unit): Job {
        return events.buffer().filterIsInstance<T>().onEach { event ->
            scope.launch {
                runCatching {
                    consumer(event)
                }.onFailure(logger::error)
            }
        }.launchIn(this)
    }

    suspend fun emitEvent(event: E) = events.emit(event)

    companion object {
        val logger = LoggerFactory.getLogger(EventDispatcher::class.java)
    }
}

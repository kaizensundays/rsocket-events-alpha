package com.kaizensundays.particles.rsocket.events

import io.rsocket.Payload
import io.rsocket.SocketAcceptor
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.TcpServerTransport
import com.kaizensundays.particles.rsocket.events.Reactor.elasticAsync
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration

/**
 * Created: Sunday 7/26/2020, 7:00 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultRSocketEndpoint {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val messageConverter = DefaultMessageConverter()

    var eventProducer: EventProducer = NopEventProducer()

    private var server: Disposable? = null

    private val defaultDelay = 3000L
    private val defaultPort = 7000

    var port = defaultPort

    private fun toMessage(payload: Payload): Message {

        val wire = payload.dataUtf8

        return messageConverter.toMessage(wire)
    }

    private fun handle(payload: Payload) {

        elasticAsync {
            val message = toMessage(payload)
            logger.info("$message")
        }
    }

    private fun disconnected() {

        elasticAsync {
            val disconnected = ClientDisconnected()
            logger.warn("$disconnected")
            eventProducer.onEvent(disconnected)
        }
    }

    private fun handle(publisher: Publisher<Payload>) {

        Flux.from(publisher)
                .doOnNext { payload -> handle(payload) }
                .doFinally { disconnected() }
                .subscribeOn(Schedulers.elastic()).subscribe()
    }

    fun start() {

        val acceptor = SocketAcceptor.forRequestChannel { publisher ->
            logger.info("forRequestChannel() >")

            handle(publisher)

            logger.info("forRequestChannel() <")
            return@forRequestChannel Flux.create<Payload> { emitter ->
                eventProducer.addEventListener(DefaultEventListener(emitter, eventProducer))
                eventProducer.onEvent(ClientConnected())
                logger.info("Event listener is ready")
            }.subscribeOn(Schedulers.elastic())
        }

        server = RSocketServer.create()
                .acceptor(acceptor)
                .bind(TcpServerTransport.create("localhost", port))
                .delaySubscription(Duration.ofMillis(defaultDelay))
                .doOnNext { channel ->
                    elasticAsync {
                        logger.info("Server started @{}", channel.address())
                    }
                }.subscribe()

        logger.info("Started")
    }

    fun stop() {

        server?.dispose()

        logger.info("Stopped")
    }

}
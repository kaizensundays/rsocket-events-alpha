package com.kaizensundays.particles.rsocket.events

import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux

/**
 * Created: Monday 7/27/2020, 7:35 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultRSocketProducer : Producer {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val messageConverter = DefaultMessageConverter()

    private var socket: RSocket? = null

    private val defaultPort = 7000

    var port = defaultPort
    var bindAddress = "localhost"

    private fun fromMessage(message: Message): Payload {

        val wire = messageConverter.fromMessage(message)

        return DefaultPayload.create(wire)
    }

    private fun toMessage(payload: Payload): Message {

        val wire = payload.dataUtf8

        return messageConverter.toMessage(wire)
    }

    override fun requestChannel(publisher: Flux<Message>): Flux<Message> {

        val channel = socket?.requestChannel(
                publisher.map { message -> fromMessage(message) }.log())

        return if (channel != null) {
            channel.log().map { payload -> toMessage(payload) }
        } else {
            Flux.just()
        }
    }

    fun start() {

        socket = RSocketConnector.connectWith(
                TcpClientTransport.create(bindAddress, port))
                .block()

        if (socket == null) {
            throw IllegalStateException()
        }

        logger.info("Started")
    }

    fun stop() {

        socket?.dispose()

        logger.info("Stopped")
    }

}
package com.kaizensundays.particles.rsocket.events

import io.rsocket.Payload
import io.rsocket.util.DefaultPayload
import com.kaizensundays.particles.rsocket.events.Reactor.elasticAsync
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.FluxSink

/**
 * Created: Sunday 7/26/2020, 1:12 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultEventListener(private val emitter: FluxSink<Payload>, private val eventProducer: EventProducer) : EventListener {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val messageConverter = DefaultMessageConverter()

    override fun onEvent(event: Event) {

        if (!emitter.isCancelled) {
            val wire = messageConverter.fromMessage(event)
            emitter.next(DefaultPayload.create(wire))
        } else {
            elasticAsync {
                logger.warn("Emitter $emitter has been canceled")
                eventProducer.removeEventListener(this)
            }
        }

    }

}
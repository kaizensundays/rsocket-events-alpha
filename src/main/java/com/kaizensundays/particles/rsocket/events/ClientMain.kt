package com.kaizensundays.particles.rsocket.events

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import java.time.Duration

/**
 * Created: Sunday 7/26/2020, 4:58 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
object ClientMain {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private const val maxEvents = 4L
    private const val defaultDelay = 3000L

    @JvmStatic
    fun main(args: Array<String>) {

        val producer = DefaultRSocketProducer()
        producer.start()

        val channel = producer.requestChannel(
                Flux.interval(Duration.ofMillis(defaultDelay), Duration.ofMillis(defaultDelay))
                        .map { i -> Heartbeat("heartbeat[$i]") }
        )

        channel.doOnNext { message -> logger.info("$message") }
                .take(maxEvents)
                .then()
                .block()


        Thread.sleep(defaultDelay)

        producer.stop()

        logger.info("Done")

    }
}
package com.kaizensundays.particles.rsocket.events

/**
 * Created: Saturday 7/25/2020, 12:30 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
object ServerMain {

    @JvmStatic
    fun main(args: Array<String>) {

        val eventProducer = DefaultEventProducer()

        val endpoint = DefaultRSocketEndpoint()
        endpoint.eventProducer = eventProducer

        eventProducer.start()
        endpoint.start()

        Runtime.getRuntime().addShutdownHook(Thread(Runnable {
            eventProducer.stop()
            endpoint.stop()
        }))

    }

}
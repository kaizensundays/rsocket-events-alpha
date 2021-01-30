package com.kaizensundays.particles.rsocket.events

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock

/**
 * Created: Sunday 7/26/2020, 8:30 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultEventProducer : EventProducer {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val defaultDelay = 3000L
    private val eventDelay = 7000L

    private val executor = Executors.newSingleThreadExecutor()

    private val listenersLock = ReentrantLock()
    private val listeners = mutableListOf<EventListener>()

    override fun onEvent(event: Event) {
        listeners.forEach { listener -> listener.onEvent(event) }
    }

    override fun addEventListener(listener: EventListener) {
        try {
            listenersLock.lock()
            listeners.add(listener)
        } finally {
            listenersLock.unlock()
        }
    }

    override fun removeEventListener(listener: EventListener) {
        try {
            listenersLock.lock()
            listeners.remove(listener)
        } finally {
            listenersLock.unlock()
        }
    }

    private val running = AtomicBoolean(true)

    private fun produceEvents() {

        executor.execute {

            Thread.sleep(defaultDelay)
            logger.info("Started producing events ...")

            var i = 0
            while (running.get()) {

                val event = Event("event[$i]")

                try {
                    listenersLock.lock()
                    if (listeners.size > 0) {
                        listeners.forEach { listener -> listener.onEvent(event) }
                        i++
                    }
                } finally {
                    listenersLock.unlock()
                }

                Thread.sleep(eventDelay)
            }

            logger.info("Stopped producing events ...")
        }

    }

    fun start() {

        produceEvents()

        logger.info("Started")
    }

    fun stop() {

        running.set(false)

        executor.shutdown()

        logger.info("Stopped")
    }

}
package com.kaizensundays.particles.rsocket.events

/**
 * Created: Sunday 7/26/2020, 9:13 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class NopEventProducer : EventProducer {

    override fun onEvent(event: Event) {
        // No-Op
    }

    override fun addEventListener(listener: EventListener) {
        // No-Op
    }

    override fun removeEventListener(listener: EventListener) {
        // No-Op
    }
}
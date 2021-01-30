package com.kaizensundays.particles.rsocket.events

/**
 * Created: Sunday 7/26/2020, 9:11 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
interface EventProducer : EventListener {

    fun addEventListener(listener: EventListener)

    fun removeEventListener(listener: EventListener)

}
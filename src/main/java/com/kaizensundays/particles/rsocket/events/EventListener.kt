package com.kaizensundays.particles.rsocket.events

/**
 * Created: Sunday 7/26/2020, 9:02 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
interface EventListener {

    fun onEvent(event: Event)

}
package com.kaizensundays.particles.rsocket.events

/**
 * Created: Wednesday 7/29/2020, 8:00 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
interface MessageConverter {

    fun fromMessage(message: Message): String

    fun toMessage(wire: String): Message

}
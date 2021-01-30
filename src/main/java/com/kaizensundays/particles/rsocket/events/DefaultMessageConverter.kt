package com.kaizensundays.particles.rsocket.events

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 * Created: Wednesday 7/29/2020, 8:02 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultMessageConverter : MessageConverter {

    private val jackson = ObjectMapper().registerModule(KotlinModule())

    override fun fromMessage(message: Message): String {
        return try {
            jackson.writeValueAsString(message)
        } catch (e: Exception) {
            "?"
        }
    }

    override fun toMessage(wire: String): Message {
        return try {
            jackson.readValue(wire, Message::class.java)
        } catch (e: Exception) {
            UnknownMessage()
        }
    }

}
package com.kaizensundays.particles.rsocket.events

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created: Wednesday 7/29/2020, 8:04 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultMessageConverterTest {

    private val converter = DefaultMessageConverter()

    private val json = arrayListOf(
            """{"@class":"com.kaizensundays.particles.rsocket.events.Event","text":"event[1]"}""",
            """{"@class":"com.kaizensundays.particles.rsocket.events.Heartbeat","text":"heartbeat[3]"}"""

    )

    @Test
    fun fromMessage() {

        assertEquals(json[0], converter.fromMessage(Event("event[1]")))
        assertEquals(json[1], converter.fromMessage(Heartbeat("heartbeat[3]")))

    }

    @Test
    fun toMessage() {

        var message = converter.toMessage(json[0])

        assertTrue(message is Event)
        assertEquals("event[1]", message.text)

        message = converter.toMessage(json[1])

        assertTrue(message is Heartbeat)
        assertEquals("heartbeat[3]", message.text)

    }

}
package com.kaizensundays.particles.rsocket.events

/**
 * Created: Monday 7/27/2020, 7:30 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
open class Event(val text: String) : Message {

    override fun toString(): String {
        return "${javaClass.simpleName}(text='$text')"
    }

}
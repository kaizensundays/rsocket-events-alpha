package com.kaizensundays.particles.rsocket.events

import reactor.core.publisher.Flux

/**
 * Created: Monday 7/27/2020, 7:35 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
interface Producer {

    fun requestChannel(publisher: Flux<Message>): Flux<Message>

}
package com.kaizensundays.particles.rsocket.events

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

/**
 * Created: Saturday 8/1/2020, 5:05 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
object Reactor {

    fun <T> elasticAsync(f: () -> T) {
        Mono.fromCallable { f.invoke() }.subscribeOn(Schedulers.elastic()).subscribe()
    }

}
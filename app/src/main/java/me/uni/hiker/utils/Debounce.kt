package me.uni.hiker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @param debounceTime Debounce time in miliseconds.
 * @param scope Scope used for launching an async code block that will run after `debounceTime` is up.
 * @param onDebounce Action to run.
 * @return A `Job` that can be used to cancel this debounce event.
 */
fun debounce(debounceTime: Long, scope: CoroutineScope, onDebounce: suspend CoroutineScope.() -> Unit): Job {
    return scope.launch {
        delay(debounceTime)

        onDebounce()
    }
}
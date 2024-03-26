package taboolib.common

import java.util.concurrent.TimeUnit

fun asyncThread(runnable: () -> Unit) {
    SimpleThreadPool.async { run(runnable) }
}

fun asyncLaterThread(runnable: () -> Unit, delay:Long, timeUnit:TimeUnit) {
    SimpleThreadPool.asyncLater({ run(runnable) }, delay, timeUnit)
}

fun asyncTimer(runnable: () -> Unit, delay: Long, period: Long, timeUnit: TimeUnit) {
    SimpleThreadPool.asyncTimer({ run(runnable) }, delay, period, timeUnit)
}
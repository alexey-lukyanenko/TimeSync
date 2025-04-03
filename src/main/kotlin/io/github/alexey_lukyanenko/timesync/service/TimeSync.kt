package io.github.alexey_lukyanenko.timesync.service

import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinBase
import io.github.alexey_lukyanenko.timesync.app.Config
import io.github.alexey_lukyanenko.timesync.lib.timezonedb.GetTimeZone
import org.apache.logging.log4j.LogManager
import java.time.Duration
import java.time.Instant
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.SECONDS

class TimeSync(private val config: Config) {
    private val logger = LogManager.getLogger()
    private val scheduler = ScheduledThreadPoolExecutor(1)
    
    init {
        scheduler.submit(::taskHandler)
    }

    private fun taskHandler() {
        val data = GetTimeZone(config.timeZone, config.apiKey)()
        val apiNow = Instant.ofEpochSecond(data.now)
        try {
            if (Duration.between(Instant.now(), apiNow).abs() > Duration.ofSeconds(5)) {
                sync(data.now)
            }
        } finally {
            scheduler.schedule(::taskHandler, 12, HOURS)
            logger.info("Set next run after 12 hours")
        }
    }

    fun sync(now: Long) {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = SECONDS.toMillis(now)
        val winTime = WinBase.SYSTEMTIME()
        winTime.fromCalendar(cal)
        Kernel32.INSTANCE.SetLocalTime(winTime)
        logger.info("Synchronized")
    }
}
package io.github.alexey_lukyanenko.timesync.service

import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinBase
import io.github.alexey_lukyanenko.timesync.app.Config
import io.github.alexey_lukyanenko.timesync.lib.timezonedb.GetTimeZone
import org.apache.logging.log4j.LogManager
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.SECONDS

class TimeSync(private val config: Config) {
    private val logger = LogManager.getLogger()
    private val scheduler = ScheduledThreadPoolExecutor(1)
    private val limit = Duration.ofSeconds(5)
    private val timeZoneNoOffset = TimeZone.getTimeZone(ZoneOffset.UTC)
    
    init {
        scheduler.submit(::taskHandler)
    }

    private fun taskHandler() {
        val data = GetTimeZone(config.timeZone, config.apiKey)()
        val apiNow = Instant.ofEpochSecond(data.now)
        val thisNow = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        try {
            val difference = Duration.between(thisNow, apiNow).abs()
            if (difference > limit) {
                sync(data.now)
                logger.info("Synchronized: difference was [{}]", difference)
            }
        } finally {
            scheduler.schedule(::taskHandler, 12, HOURS)
            logger.info("Set next run after 12 hours")
        }
    }

    private fun sync(now: Long) {
        val cal = Calendar.getInstance(timeZoneNoOffset)
        cal.timeInMillis = SECONDS.toMillis(now)
        val winTime = WinBase.SYSTEMTIME()
        winTime.fromCalendar(cal)
        Kernel32.INSTANCE.SetLocalTime(winTime)
    }

    fun stop() {
        scheduler.shutdownNow()
    }
}
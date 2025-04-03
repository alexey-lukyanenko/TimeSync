package io.github.alexey_lukyanenko.timesync.lib.timezonedb

class GetTimeZoneDto(
    val isDst: Boolean,
    val zoneStartsUTC: Long,
    val zoneEndsUTC: Long,
    val offset: Long,
    val now: Long
)

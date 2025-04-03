package io.github.alexey_lukyanenko.timesync.lib.timezonedb

import com.eclipsesource.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GetTimeZone(private val timeZone: String, private val apiKey: String) {
    private fun sendRequest(): String {
        val uri = "https://api.timezonedb.com/v2.1/get-time-zone?key=${apiKey}&format=json&by=zone&zone=${timeZone}&fields=timestamp,dst,zoneStart,zoneEnd,gmtOffset"
        val request = HttpRequest.newBuilder().uri(URI.create(uri)).build()
        val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
    
    private fun parse(contentJson: String): GetTimeZoneDto {
        val obj = Json.parse(contentJson).asObject()
        return GetTimeZoneDto(
            isDst = obj["dst"].asString().toInt() != 0,
            zoneStartsUTC = obj["zoneStart"].asLong(),
            zoneEndsUTC = obj["zoneEnd"].asLong(),
            now = obj["timestamp"].asLong(),
            offset = obj["gmtOffset"].asLong(),
        )
    }
    
    operator fun invoke(): GetTimeZoneDto {
        return parse(sendRequest())
    }
}
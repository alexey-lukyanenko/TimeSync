package io.github.alexey_lukyanenko.timesync.app

import java.nio.file.Path
import java.util.Properties
import kotlin.io.path.exists

class Config(
    val timeZone: String,
    val apiKey: String
)

fun loadConfig(appName: String): Config {
    val fileNames = listOf(Path.of("conf", "$appName.settings"), Path.of("$appName.settings"))
    fileNames.forEach { file ->
        if (file.exists()) {
            val properties = Properties()
            properties.load(file.toFile().inputStream())
            return Config(
                timeZone = (properties["timeZone"] as String).removeSurrounding("\""),
                apiKey = (properties["apiKey"] as String).removeSurrounding("\"")
            ) 
        }
    }
    throw RuntimeException("Config not loaded")
}



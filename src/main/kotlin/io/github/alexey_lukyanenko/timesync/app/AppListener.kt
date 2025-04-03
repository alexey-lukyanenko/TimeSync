package io.github.alexey_lukyanenko.timesync.app

import io.github.alexey_lukyanenko.timesync.service.TimeSync
import org.apache.logging.log4j.LogManager
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

class AppListener : ServletContextListener {
    private lateinit var service: TimeSync
    
    override fun contextInitialized(event: ServletContextEvent) {
        val appName = event.servletContext.contextPath.removePrefix("/")
        try {
            val config = loadConfig(appName)
            service = TimeSync(config)
        } catch (e: Exception) {
            LogManager.getLogger().error("Initialization failed", e)
        }
    }

    override fun contextDestroyed(event: ServletContextEvent) {}
}
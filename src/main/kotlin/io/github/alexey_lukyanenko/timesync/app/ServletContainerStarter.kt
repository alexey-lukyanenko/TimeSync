package io.github.alexey_lukyanenko.timesync.app

import javax.servlet.ServletContainerInitializer
import javax.servlet.ServletContext
import javax.servlet.annotation.HandlesTypes

@HandlesTypes(ServletContainerStarter::class)
class ServletContainerStarter: ServletContainerInitializer {
    override fun onStartup(initializerClasses: Set<Class<*>>?, servletContext: ServletContext) {
        servletContext.addListener(AppListener())
    }
}

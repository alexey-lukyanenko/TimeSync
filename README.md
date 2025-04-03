# TimeSync
A time synchronization app running in a servlet container for Windows.

The application uses [api.timezonedb.com]() to get current time.
Currently, the app checks time difference between server and local computer every 12 hours, and synchronizes if the difference more than 5 seconds.

### Uses:

- JVM 11
- Kotlin 1.9
- Log4j2 2.22.0
- JNA 5.17.0
- minimal-json 0.9.5

### Requires:

- Windows
- Servlet container (I use Tomcat, but other containers should be good too)
- API key for api.timezonedb.com

### Setup

1. Create a configuration file named `TimeSync.settings` in the container's root directory or `conf` subdirectory with the content:
```properties
timeZone=
apiKey=
```
and specify valid key and time zone name.
2. Deploy `TimeZone.war` to servlet container.
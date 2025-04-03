plugins {
    kotlin("jvm") version "1.9.23"
    war
}

group = "io.github.alexey-lukyanenko"
version = "1.0-SNAPSHOT"
val versionLog4j = "2.22.0"
val versionJna = "5.17.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-api:$versionLog4j")
    implementation("org.apache.logging.log4j:log4j-core:$versionLog4j")
    implementation("net.java.dev.jna:jna-platform-jpms:$versionJna")
    implementation("com.eclipsesource.minimal-json:minimal-json:0.9.5")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")
    testImplementation(kotlin("test"))
}

tasks {
    val serviceProviderResources by registering {
        val fileName = "META-INF/services/javax.servlet.ServletContainerInitializer"
        val genResources = project.layout.buildDirectory.dir("generated-resources")
        outputs.dir(genResources)
        doLast {
            genResources.get().file(fileName).asFile.apply { 
                parentFile.mkdirs()
                val artifactName = "${rootProject.group}.${rootProject.name}"
                val packageName = artifactName.replace("-", "_").lowercase()
                writeText("$packageName.app.ServletContainerStarter")   
            }
        }
    }
    withType<War>{
        archiveFileName.set("${rootProject.name}.war")
    }
    processResources {
        project.ext.set("artifactId", project.name)
        exclude { it.name.endsWith(".settings") }
        filesMatching("**/log4j2.xml") {
            expand(project.properties)
        }
        from(serviceProviderResources)
    }
    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(11)
}
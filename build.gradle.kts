group = "fi.utu.tech"
version = "2.0.1"

plugins {
    java
    // https://github.com/openjfx/javafx-gradle-plugin/releases
    id("org.openjfx.javafxplugin") version "0.0.10"
    // https://github.com/beryx/badass-jlink-plugin
    id("org.beryx.jlink") version "2.24.4"
    application
    `maven-publish`
}

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_11

    sourceSets["main"].java {
        srcDir("src/main/java")
    }
    sourceSets["main"].resources {
        srcDir("src/main/resources")
    }
    sourceSets["test"].java {
        srcDir("src/test/java")
    }
    sourceSets["test"].resources {
        srcDir("src/test/resources")
    }
}

javafx {
    version = "17.0.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.media", "javafx.web")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://ftdev.utu.fi/maven2")
}

dependencies {
    implementation("fi.utu.tech", "hotreload", "2.0.1")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.8.2")
    testImplementation("org.junit.jupiter", "junit-jupiter-engine", "5.8.2")
    testImplementation("org.junit.jupiter", "junit-jupiter-params", "5.8.2")
    testImplementation("org.junit.platform", "junit-platform-commons", "1.8.2")
    testImplementation("net.jqwik", "jqwik", "1.6.2")
}

application{
    mainModule.set("fi.utu.tech.gui.javafx")
    mainClass.set("fi.utu.tech.gui.javafx.Main")
    applicationDefaultJvmArgs = arrayListOf("-ea")
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.encoding = "UTF-8"
    }
    
    getByName<ProcessResources>("processResources") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE // allow duplicates
    }
    getByName<ProcessResources>("processTestResources") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE // allow duplicates
    }
}

jlink {
    launcher {
        name = "launch"
    }

    imageZip.set(project.file("${project.buildDir}/image-zip/template-javafx-image.zip"))
}

plugins {
    `java-library`
    `maven-publish`
    signing

    id("nebula.maven-resolved-dependencies") version "17.3.2"
    id("nebula.release") version "15.3.1"
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"

    id("nebula.maven-publish") version "17.3.2"
    id("nebula.contacts") version "5.1.0"
    id("nebula.info") version "11.1.0"

    id("org.openrewrite.rewrite") version "latest.release"
}

group = "com.azure.spring.migration"
description = "Recipe to migrate ASA"

repositories {
    mavenCentral()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/azure/azure-spring-rewrite")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("azure-spring-rewrite") {
            from(components["java"])
        }
    }
}

configure<nebula.plugin.release.git.base.ReleasePluginExtension> {
    defaultVersionStrategy = nebula.plugin.release.NetflixOssStrategies.SNAPSHOT(project)
}

project.rootProject.tasks.getByName("final").dependsOn(project.tasks.getByName("publishAzure-spring-rewritePublicationToGitHubPackagesRepository"))
project.rootProject.tasks.getByName("snapshot").dependsOn(project.tasks.getByName("publishAzure-spring-rewritePublicationToGitHubPackagesRepository"))

nexusPublishing {
    repositories {
        sonatype()
    }
}

val rewriteSpringVersion = "4.32.0"
dependencies {
    implementation("org.openrewrite.recipe:rewrite-spring:${rewriteSpringVersion}")
    implementation("org.openrewrite:rewrite-java")
    testImplementation("org.openrewrite:rewrite-test")
    testImplementation("org.openrewrite.recipe:rewrite-testing-frameworks:latest.release")

    compileOnly("org.projectlombok:lombok:latest.release")
    annotationProcessor("org.projectlombok:lombok:latest.release")
}
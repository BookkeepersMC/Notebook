import net.darkhax.curseforgegradle.TaskPublishCurseForge;
plugins {
    java
    idea
    `maven-publish`
    id("fabric-loom") version("1.4-SNAPSHOT")
    id("com.modrinth.minotaur") version("2.+")
    id("net.darkhax.curseforgegradle") version("1.1.18")
}

val mod_id: String by project
val mod_name: String by project
val version: String by project
val minecraft_version: String by project
val fabric_version: String by project
val fabric_loader_version: String by project

base {
    archivesName.set("Notebook-api-fabric-${minecraft_version}")
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")
    implementation("com.google.code.findbugs:jsr305:3.0.1")
    compileOnly(project(":common"))
}

loom {
    if (project(":common").file("src/main/resources/${mod_id}.accesswidener").exists())
        accessWidenerPath.set(project(":common").file("src/main/resources/${mod_id}.accesswidener"))

    @Suppress("UnstableApiUsage")
    mixin { defaultRefmapName.set("${mod_id}.refmap.json") }

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run")
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("notebook-api")
    versionNumber.set("${project.version}")
    versionName.set("Notebook API Fabric ${project.version}")
    versionType.set("release")
    uploadFile.set(tasks.remapJar)
    gameVersions.addAll("1.20.4")
    changelog = rootProject.file("CHANGELOG.md").readText()
    loaders.add("fabric")
    dependencies {
        required.project("fabric-api")
    }
}

tasks.register<TaskPublishCurseForge>("curseforge") {
    apiToken = System.getenv("CURSEFORGE_TOKEN")
    val mainFile = upload(971909, tasks.remapJar.get())
    mainFile.releaseType = "release"
    mainFile.displayName = "Notebook API Fabric ${version}"
    mainFile.addGameVersion("1.20.4")
    mainFile.addModLoader("Fabric")
    mainFile.addJavaVersion("Java 17")
    mainFile.changelog = rootProject.file("CHANGELOG.md").readText()
    mainFile.changelogType = "markdown"
    mainFile.addRequirement("fabric-api")
}
tasks {
    withType<JavaCompile> { source(project(":common").sourceSets.main.get().allSource) }

    javadoc { source(project(":common").sourceSets.main.get().allJava) }

    named("sourcesJar", Jar::class) { from(project(":common").sourceSets.main.get().allSource) }

    processResources { from(project(":common").sourceSets.main.get().resources) }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }

    repositories {
        maven(System.getenv("MAVEN_URL"))
    }
}

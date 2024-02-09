import net.darkhax.curseforgegradle.TaskPublishCurseForge;
plugins {
    idea
    `maven-publish`
    id("net.minecraftforge.gradle") version "6.+"
    id("org.spongepowered.mixin") version "0.7-SNAPSHOT"
    id("com.modrinth.minotaur") version("2.+")
    id("net.darkhax.curseforgegradle") version("1.1.18")
}

val mod_id: String by project
val mod_name: String by project
val version: String by project
val minecraft_version: String by project
val forge_version: String by project

base {
    archivesName.set("Notebook-api-forge-${minecraft_version}")
}

mixin {
    add(sourceSets.main.get(), "$mod_id.refmap.json")
    config("$mod_id.mixins.json")
    config("$mod_id.forge.mixins.json")
}

minecraft {
    mappings("official", minecraft_version)

    copyIdeResources = true //Calls processResources when in dev

    // Automatically enable forge AccessTransformers if the file exists
    // This location is hardcoded in Forge and can not be changed.
    // https://github.com/MinecraftForge/MinecraftForge/blob/be1698bb1554f9c8fa2f58e32b9ab70bc4385e60/fmlloader/src/main/java/net/minecraftforge/fml/loading/moddiscovery/ModFile.java#L123
    val transformerFile = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (transformerFile.exists())
        accessTransformer(transformerFile)

    runs {
        create("client") {
            workingDirectory(project.file("run"))
            ideaModule("${rootProject.name}.${project.name}.main")
            taskName("Client")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods {
                create("modRun") {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run"))
            ideaModule("${rootProject.name}.${project.name}.main")
            taskName("Server")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods {
                create("modServerRun") {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }

        create("data") {
            workingDirectory(project.file("run"))
            ideaModule("${rootProject.name}.${project.name}.main")
            args(
                "--mod", mod_id,
                "--all",
                "--output", file("src/generated/resources").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
            taskName("Data")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods {
                create("modDataRun") {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }
    }
}
modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("notebook-api")
    versionNumber.set("${project.version}")
    versionName.set("Notebook API Forge ${project.version}")
    versionType.set("release")
    uploadFile.set(tasks.jar)
    gameVersions.addAll("1.20.4")
    changelog = rootProject.file("CHANGELOG.md").readText()
    loaders.add("forge")
}

tasks.register<TaskPublishCurseForge>("curseforge") {
    apiToken = System.getenv("CURSEFORGE_TOKEN")
    val mainFile = upload(971909, tasks.jar.get())
    mainFile.releaseType = "release"
    mainFile.displayName = "Notebook API Forge ${project.version}"
    mainFile.addGameVersion("1.20.4")
    mainFile.addModLoader("Forge")
    mainFile.addJavaVersion("Java 17")
    mainFile.changelog = rootProject.file("CHANGELOG.md").readText()
    mainFile.changelogType = "markdown"
}
sourceSets.main.get().resources.srcDir("src/generated/resources")

dependencies {
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")
    compileOnly(project(":common"))
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")
}

tasks {
    withType<JavaCompile> { source(project(":common").sourceSets.main.get().allSource) }

    javadoc { source(project(":common").sourceSets.main.get().allJava) }

    named("sourcesJar", Jar::class) { from(project(":common").sourceSets.main.get().allSource) }

    processResources { from(project(":common").sourceSets.main.get().resources) }

    jar { finalizedBy("reobfJar") }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = base.archivesName.get()
            artifact(tasks.jar)
            fg.component(this)
        }
    }

    repositories {
        maven(System.getenv("MAVEN_URL"))
    }
}

sourceSets.forEach {
    val dir = layout.buildDirectory.dir("sourceSets/${it.name}")
    it.output.setResourcesDir(dir)
    it.java.destinationDirectory.set(dir)
}

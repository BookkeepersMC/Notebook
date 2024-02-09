import net.darkhax.curseforgegradle.TaskPublishCurseForge
plugins {
    idea
    `maven-publish`
    id("net.neoforged.gradle.userdev") version "7.+"
    `java-library`
    id("com.modrinth.minotaur") version("2.+")
    id("net.darkhax.curseforgegradle") version("1.1.18")
}

val mod_id: String by project
val mod_name: String by project
val version: String by project
val minecraft_version: String by project
val neoforge_version: String by project

base {
    archivesName.set("Notebook-api-neoforge-$minecraft_version")
}

// Automatically enable neoforge AccessTransformers if the file exists
// This location is hardcoded in FML and can not be changed.
// https://github.com/neoforged/FancyModLoader/blob/a952595eaaddd571fbc53f43847680b00894e0c1/loader/src/main/java/net/neoforged/fml/loading/moddiscovery/ModFile.java#L118
val transformerFile = file("src/main/resources/META-INF/accesstransformer.cfg")
if (transformerFile.exists())
    minecraft.accessTransformers.file(transformerFile)

runs {
    configureEach { modSource(project.sourceSets.main.get()) }

    create("client") { systemProperty("neoforge.enabledGameTestNamespaces", mod_id) }

    create("server") {
        systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        programArgument("--nogui")
    }

    create("gameTestServer") { systemProperty("neoforge.enabledGameTestNamespaces", mod_id) }

    create("data") {
        programArguments.addAll(
            "--mod", mod_id,
            "--all",
            "--output", file("src/generated/resources").absolutePath,
            "--existing", file("src/main/resources/").absolutePath
        )
    }
}
modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("notebook-api")
    versionNumber.set("${project.version}")
    versionName.set("Notebook API NeoForge ${project.version}")
    versionType.set("release")
    uploadFile.set(tasks.jar)
    gameVersions.addAll("1.20.4")
    changelog = rootProject.file("CHANGELOG.md").readText()
    loaders.add("neoforge")
}


tasks.register<TaskPublishCurseForge>("curseforge") {
    apiToken = System.getenv("CURSEFORGE_TOKEN")
    val mainFile = upload(971909, tasks.jar.get())
    mainFile.releaseType = "release"
    mainFile.displayName = "Notebook API NeoForge ${project.version}"
    mainFile.addGameVersion("1.20.4")
    mainFile.addModLoader("NeoForge")
    mainFile.addJavaVersion("Java 17")
    mainFile.changelog = rootProject.file("CHANGELOG.md").readText()
    mainFile.changelogType = "markdown"
}
sourceSets.main.get().resources.srcDir("src/generated/resources")

dependencies {
    implementation("net.neoforged:neoforge:$neoforge_version")
    compileOnly(project(":common"))
}

// NeoGradle compiles the game, but we don't want to add our common code to the game's code
val notNeoTask: Spec<Task> = Spec { !it.name.startsWith("neo") }

tasks {
    withType<JavaCompile>().matching(notNeoTask).configureEach { source(project(":common").sourceSets.main.get().allSource) }

    withType<Javadoc>().matching(notNeoTask).configureEach { source(project(":common").sourceSets.main.get().allJava) }

    named("sourcesJar", Jar::class) { from(project(":common").sourceSets.main.get().allSource) }

    processResources { from(project(":common").sourceSets.main.get().resources) }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = base.archivesName.get()
            artifact(tasks.jar)
        }
    }

    repositories {
        maven(System.getenv("MAVEN_URL"))
    }
}

# [Notebook API](https://curseforge.com/minecraft/mc-mods/notebook-api)

This is a API for working with resources in the "Common" module.

# Installation 
To install this mod on the client, place the `.jar` file in your `mods` folder.

For servers, place the file in the `mods` folder on the server.

## Compatibility

This mod is compatible with Fabric, Quilt, Forge, and NeoForge.
- Minecraft 1.20.4
- Fabric Loader 0.15.7 or higher
- Forge 49.0.31 or higher
- Neoforge 20.4.190 or higher
- Fabric API 0.96.4+1.20.4 or higher for fabric

## Developing Guide
Notebook API uses the normal Gradle project structure, and can be compiled by running the gradle task `build`.

## Implementation in your mod
To implement this mod, follow the steps below.

### Maven
Add the maven repo for CrypticVerse's Resources
```gradle
repositories {
    maven { url = "https://raw.githubusercontent.com/CrypticVerse/projectresources/master/maven" }
}
```

### Implementation
```gradle
// mcversion is the targeted MC version, like 1.20.4
// modversion is the Notebook version, ex. 1.1.0.6

// Common
implementation "com.bookkeepersmc:Notebook-api-common-mcversion:modversion"

// Fabric
include(modImplementation "com.bookkeepersmc:Notebook-api-fabric-mcversion:modversion")

// Forge
implementation fg.deobf("com.bookkeepersmc:Notebook-api-forge-mcversion:modversion")

// NeoForge
implementation ("com.bookkeepersmc:Notebook-api-neoforge-mcversion:modversion")
```

#### Requirements

- OpenJDK 17 or higher.
  We recommend using the JDK from [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=17), as it works without issues.

- Gradle 8.6 or higher (optional)
  Gradle is already provided in this repository, so you do not need to download it seperatley

## License
   This mod is licensed under MIT License, which is free and open source. For more info, refer to the [license file](LICENSE).
 


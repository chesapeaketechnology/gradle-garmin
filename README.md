# gradle-garmin

Gradle plugins to assist with Garmin wearable development

## Adding the plugin to your project
### Plugin DSL
**Apps**
```groovy
plugins {
  id "com.chesapeaketechnology.gradle-garmin-app" version "0.0.1-SNAPSHOT"
}
```
**Barrels**

```groovy
plugins {
  id "com.chesapeaketechnology.gradle-garmin-barrel" version "0.0.1-SNAPSHOT"
}
```

### Legacy
OR via the legacy method:
```groovy
buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath group: 'com.chesapeaketechnology',
                name: 'gradle-garmin',
                version: '0.0.1-SNAPSHOT'
    }
}
```

**Apps**
```groovy
apply plugin: 'com.chesapeaketechnology.gradle-garmin-app'
```

**Barrels**
```groovy
apply plugin: 'com.chesapeaketechnology.gradle-garmin-barrel' 
```

## Sample Usage

### Setting Environment variables
There are two environment variables that can be set which reduces the amount of configuration needed in the Gradle 
script. It is recommended that these be set to generify the build.

* `GARMIN_SDK_HOME` - The path to your Garmin SDK installation
* `GARMIN_DEV_KEY` - The location of the user generated Garmin developer key. Information on generating a developer key
for signing your Garmin applications can be found in the Garmin [Getting Started Guide](https://developer.garmin.com/connect-iq/programmers-guide/getting-started).

### Configuration
Both Apps and Barrels share some of the same configuration items.

* `sdkDirectory` - (REQUIRED if `GARMIN_SDK_HOME` is not set) Location of the Garmin SDK install
* `appDirectory` - (OPTIONAL) (DEFAULT: `<Project Directory>`) base directory for the app/barrel
* `outputDirectory` - (OPTIONAL) (DEFAULT: `<Java Build Directory>`) directory for generated output
* `jungleFiles` - (OPTIONAL) (DEFAULT: `<Project Directory>/monkey.jungle`) array of jungle files to compile with
* `outName` - (OPTIONAL) (DEFAULT: `<Project Name>`) the generated file name(s)

#### Garmin Apps

For the Garmin App plugin, use the `garminApp` extension. In addition to the shared configuration items listed above, 
the following configuration options are available:

* `developerKey` - (REQUIRED if `GARMIN_DEV_KEY` is not set) The developer key used to compile the binaries
* `devices` - (REQUIRED) List of devices to produce binaries
* `parallel` - (OPTIONAL) (DEFAULT: `true`) Whether or not to compile all devices in parallel

Example:
```groovy
garminApp {
    developerKey = "/Users/JCampbell8/keys/developer_key" //not needed if GARMIN_DEV_KEY is set
    sdkDirectory = "/Users/JCampbell8/SDKs/connectiq-sdk-mac-3.1.7-2020-01-23-a3869d977" //not needed if GARMIN_SDK_HOME is set
    targetDevices = ["fenix5plus", "fenix6pro"]
}
```

#### Garmin Barrels

For building a Garmin barrel, use the `garminBarrel` extension.

Example:
```groovy
garminBarrel {
    outName = "MySpecialBarrel"
    sdkDirectory = "/Users/JCampbell8/SDKs/connectiq-sdk-mac-3.1.7-2020-01-23-a3869d977" //not needed if GARMIN_SDK_HOME is set
}
```

### Building
Both plugins extends the JavaPlugin so building is as simple as running `gradlew build`. However, specific tasks for
building each type of project are provided.

* Apps: `gradlew buildGarminApp`
* Barrels: `gradlew buildGarminBarrel`
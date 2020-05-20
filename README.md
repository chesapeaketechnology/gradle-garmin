# gradle-garmin

Gradle plugins to assist with Garmin wearable development

## Adding the plugin to your project
### Plugin DSL
**Apps**
```groovy
plugins {
  id "com.chesapeaketechnology.gradle-garmin-app" version "0.2.1"
}
```
**Barrels**

```groovy
plugins {
  id "com.chesapeaketechnology.gradle-garmin-barrel" version "0.2.1"
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
                version: '0.2.1'
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

*WINDOWS USERS* - Make sure your dev key path contains the dev key extension - ie. `C:\\<your_path>\\developer_key.der`
### Configuration
Both Apps and Barrels share some of the same configuration items.

| Property      | Description  | Required      | Default  |
| ------------- | ------------- | ------------- | ------------ |
| `sdkDirectory`  | Location of the Garmin SDK install   | **Yes** - if GARMIN_SDK_HOME is not set | N/A
| `appDirectory`  | Base directory for the app/barrel      |   **No** | `<Project Directory>`
| `outputDirectory` | Directory for generated output     |    **No** | `<Java Build Directory>`
| `jungleFiles` | Array of jungle files to compile with    |    **No** | `<Project Directory>/monkey.jungle`
| `outName` | The generated file name(s)     |    **No** | `<Project Name>`


#### Garmin Apps

For the Garmin App plugin, use the `garminApp` extension. In addition to the shared configuration items listed above, 
the following configuration options are available:

| Property      | Description  | Required      | Default  |
| ------------- | ------------- | ------------- | ------------ |
| `developerKey`  | The developer key used to compile the binaries  | **Yes** - if `GARMIN_DEV_KEY` is not set | N/A
| `devices`  | List of devices to produce binaries      |   **Yes** | N/A
| `parallel` | Whether or not to compile all devices in parallel     |    **No** | `true`

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

### Testing
To run unit tests, the test configuration can be added to both apps and barrels. Adding the test configuration
is as simple as:

```groovy
garminApp {
    appName = 'my-app'
    targetDevices = ["fenix5plus", "fenix6pro"]

    test {
        device = 'fenix5plus'
    }
}
```

The `device` tag is a required element and tells the test phase which device to simulate and run tests against.

Once the configuration is set, execute `gradle test` to run all unit tests.

### Publishing
This is a new feature that isn't available in Garmin's SDK or via the Eclipse plugin. Built artifacts can now be published to 
maven repositories by default.

In either an app or barrel project, execute `gradle publishToMavenLocal` which will publish the artifact to your local
maven repository.

Any repository configured according to the [maven-publish](https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven:repositories)
instruction set will be available as a target repository for Garmin project builds.

```groovy
publishing {
    repositories {
        maven {
            // change to point to your repo, e.g. http://my.org/repo
            url = "$buildDir/repo"
        }
    }
}
```
Running `gradle publish` will publish your build artifact(s) to all of your defined repositories. 

#### Barrel Dependencies
Another new and useful feature is the ability to depend on published barrel artifacts from any configured
repository. Simply use the `barrel` scope in front of any barrel dependency:

```groovy
dependencies {
    barrel "com.test:my-awesome-barrel:1.0.0@barrel"
}
```
Since barrel files have a `.barrel` extension, notice that we also have to specify the extension with the `@` notation.
# gradle-garmin
Gradle plugins to assist with Garmin wearable development

## Sample Usage
To add the plugin to your project:

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

### Garmin Apps
Add the plugin:
```groovy
apply plugin: 'com.chesapeaketechnology.gradle-garmin-app'
```

For a Garmin App plugin, use the `garminApp` extension.

```groovy
garminApp {
    outputDirectory = "bin"
    developerKey = "/Users/jcampbell/Documents/developer_key"
    sdkDirectory = "/Users/jcampbell/Code/connectiq-sdk-mac-3.1.7-2020-01-23-a3869d977"
    targetDevices = ["fenix5plus", "fenix6pro"]
}
```

Then run `gradlew buildGarminApp`

### Garmin Barrels
Add the plugin:
```groovy
apply plugin: 'com.chesapeaketechnology.gradle-garmin-barrel' 
```
For building a Garmin barrel, use the `garminBarrel` extension:

```groovy
garminBarrel {
    sdkDirectory = "/Users/jcampbell/Code/connectiq-sdk-mac-3.1.7-2020-01-23-a3869d977"
}
```

Then run `gradlew buildGarminBarrel`
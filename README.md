# simple-semantic-version

This is a gradle plugin that helps you easily do semantic versioning of java projects using gradle.

---

# Usage

## Setting build.gradle

### from Local File
```gradle
buildscript {
    dependencies {
        classpath files('libs/gradle-semantic-versioning-manager-plugin-0.1.0.jar')
    }
}

plugins {
  // ~~~
}

apply plugin: 'io.github.mainmethod0126.simple-semantic-version'
```
### from Gradle Plugin Portal
Currently, it cannot be used because it is not registered in the Gradle Plugin Portal. We will update the document after registration as soon as possible.


## Example

### Version Increment

#### Major version
```bash
gradlew build -Pmajor=++
```

#### Minor version
```bash
gradlew build -Pminor=++
```

#### Patch version
```bash
gradlew build -Ppatch=++
```

#### PrereleaseVersion(pr) version
PrereleaseVersion Since version is in text format, incremental method is not supported.

#### BuildMetadata(bm) version
BuildMetadataVersion Since version is in text format, incremental method is not supported.

### Version Change

#### Major version
```bash
gradlew build -Pmajor=1
```

#### Minor version
```bash
gradlew build -Pminor=2
```

#### Patch version
```bash
gradlew build -Ppatch=3
```

#### PrereleaseVersion(pr) version
```bash
gradlew build -Ppr=beta
```

#### BuildMetadata(bm) version
```bash
gradlew build -Pbm=test
```

#### Java Version
```bash
gradlew build -Pbm=test
```


#### Compositive
```bash
gradlew build -Pmajor=1 -Pminor=2 -Ppatch=3 -Ppr=test -Pbm=test
```

## Build Artifacts Sample

build option
```bash
gradlew build -Pmajor=1 -Pminor=2 -Ppatch=3 -Ppr=test -Pbm=test
```
### Final path and directory
```bash
/dist/<YYYY-MM-DD>/<java version>/<version>/app-1.2.3-beta+test.jar
```
#### Directory : dist
A folder called "dist" is automatically created in the application root path, and Build Artifacts are located under the "dist" folder. (If it already exists, the creation process is skipped)

#### Directory : YYYY-MM-DD
The year, month, and day information of the build is created as a directory. (ex: 2023-02-07)

#### Directory : java version
During gradle build, the java version entered through -Pjavav is created as a directory. (When the javav option is omitted, the build java version is used by default)

#### Directory : version
The directory is created with the final version information generated during Gradle build. (ex : app-1.2.3-beta-test)


---

# Build and Test

TODO: Describe and show how to build your code and run the tests. 

---

# Contribute

TODO: Explain how other users and developers can contribute to make your code better. 

If you want to learn more about creating good readme files then refer the following [guidelines](https://docs.microsoft.com/en-us/azure/devops/repos/git/create-a-readme?view=azure-devops). You can also seek inspiration from the below readme files:
- [ASP.NET Core](https://github.com/aspnet/Home)
- [Visual Studio Code](https://github.com/Microsoft/vscode)
- [Chakra Core](https://github.com/Microsoft/ChakraCore)

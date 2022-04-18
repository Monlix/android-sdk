# Monlix Android SDK

Monlix offerwall for Adndroid

# Integration


1. In the top level build.gradle file, add the following

For Gradle < 7
```groovy
allprojects {
   repositories {
   ...
   maven { url 'https://jitpack.io' }
   }
 }
```

For Gradle > 7, in the settings.gradle , add maven { url 'https://jitpack.io' }
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

2. In the app level build.gradle file, add the dependency

```groovy
 implementation 'com.github.Monlix:android-sdk:v0.1.4'
```

3. In the app level build.gradle file, enable the **dataBinding** feature

```groovy
 buildFeatures {  
  dataBinding true  
}
```

# Usage



----------

1. Load the library in **Activity/Fragment**

Kotlin
```groovy
  MonlixOffers.createInstance(this,"APP_ID","USER_ID")
```
Java
```groovy
  MonlixOffers.INSTANCE.createInstance(this,"APP_ID","USER_ID")
```

2. Show the offerwall

Kotlin
```groovy
  MonlixOffers.showWall(this)
```

JAVA
```groovy
  MonlixOffers.INSTANCE.showWall(this)
```

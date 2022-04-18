# Monlix Android SDK

Monlix offerwall for Adndroid

# Integration

## Import from JitPack repository

1. In the top level build.gradle file, add the following

```groovy
allprojects {
   repositories {
   ...
   maven { url 'https://jitpack.io' }
   }
 }
```

2. In the app level build.gradle file, add the dependency

```groovy
 implementation 'com.github.netboxify:monlix-android-sdk:0.0.4'
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

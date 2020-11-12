## A utility sealed class for handling failure & success.

[![Build](https://github.com/seanghay/result-of/workflows/Java%20CI/badge.svg)](https://travis-ci.org/seanghay/result-of)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Bintray](https://api.bintray.com/packages/seanghay/maven/resultof/images/download.svg) ](https://bintray.com/seanghay/maven/resultof/_latestVersion)


This is not the same as standard Kotlin `Result` because it is a **sealed class not an inline class**.

### Installation

```groovy
dependencies {
    implementation("com.seanghay.resultof:resultof:1.0.0")
}
```

### Basic Usage

```kotlin
val result = resultOf {
    fetchUsers()
}

result.onSuccess { users -> println(users) }
result.onFailure { throwable -> println(throwable) }
```

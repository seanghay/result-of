## A utility sealed class for handling failure & success.

This is not the same as standard Kotlin `Result` because it is a **sealed class not an inline class**.

### Basic Usage

```kotlin
val result = resultOf {
    fetchUsers()
}

result.onSuccess { users -> println(users) }

result.onFailure { throwable -> println(throwable) }
```


```groovy
dependencies {
    implementation("com.seanghay.resultof")
}
```
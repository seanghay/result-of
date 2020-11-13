## A utility sealed class for handling failure & success.

[![Build](https://github.com/seanghay/result-of/workflows/Java%20CI/badge.svg)](https://travis-ci.org/seanghay/result-of)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Bintray](https://api.bintray.com/packages/seanghay/maven/resultof/images/download.svg) ](https://bintray.com/seanghay/maven/resultof/_latestVersion)


This is not the same as standard Kotlin `Result` because it is a **sealed class not an inline class**.

### Installation

```groovy
dependencies {
    implementation("com.seanghay:resultof:1.0.0")
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

### Usage with LiveData

```kotlin
class MyViewModel : ViewModel() {
    val profile = liveData {
        val result = resultOf { userRepository.getMyProfile() }
        emit(result)
    }
}
```

```kotlin
class MyFragment: Fragment() {
    
    private val viewModel: MyViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    
        // observe user profile result
        viewModel.profile.observe(viewLifecycleOwner) { result -> 
            
            // we only need `profileUrl` so we map it.
            result.map { it.profileUrl }.onSuccess { url ->
                Glide.with(this)
                    .load(url)
                    .into(binding.imageView)
            }

            result.onFailure { throwable -> 
                Toast.makeText(
                    requireContext(), 
                    throwable?.localizedMessage ?: "Oops!", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
```

### Transformations

- `map` Map successful value into something else
- `flatMap` Map another `ResultOf<T>` into anther `ResultOf<R>`
- `failureMap` Map throwable of the Failure into another throwable. For example, map `IllegalStateException` to `MyException`
- `failureFlatMap` same as `flatMap` but for Failure


### Retrieving Value & Throwable

- `result.valueOrNull`
- `result.valueOrThrow`
- `result.valueOrDefault { "my-default-value" }`
- `result.throwableOrNull`


### Conversion

We can convert from standard Kotlin Result into ResultOf by calling `asResultOf()` on Result object.

```kotlin
val result = runCatching { 12345 }.asResultOf()
```


### Nullable ResultOf to Non-null ResultOf Conversion

Most of the time, we don't want `null` value to be the successful value, so we want it to be a Failure instead.
We can do that by calling `failureOnNull()`

```kotlin
val nullableResult: ResultOf<String?> = resultOf { null }
val nonNullResult: ResultOf<String> = nullableResult.failureOnNull()

println(nullableResult.isFailure)
// false

println(nonNullResult.isFailure)
// true
```

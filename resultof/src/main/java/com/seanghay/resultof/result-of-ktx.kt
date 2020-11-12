/**
 * Copyright 2020 Seanghay Yath (@seanghay)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package com.seanghay.resultof

/**
 * Check whether ResultOf is successful or failed
 * @return true if ResultOf.Success
 */
inline val <R> ResultOf<R>.isSuccessful: Boolean
  get() = this is ResultOf.Success

/**
 * Check whether ResultOf is successful or failed
 * @return true if ResultOf.Failure
 */
inline val <R> ResultOf<R>.isFailure: Boolean
  get() = this is ResultOf.Failure

/**
 * @return the value or null if it is a ResultOf.Failure
 */
inline val <R> ResultOf<R>.valueOrNull: R?
  get() = if (this is ResultOf.Success) value else null

/**
 * @return the value or throw an exception if failure
 */
inline val <R> ResultOf<R?>.valueOrThrow: R
  get() = if (this is ResultOf.Success) value!!
  else throw throwableOrNull ?: NullPointerException("value == null")

/**
 * @return throwable value or null of the ResultOf<R>
 */
inline val <R> ResultOf<R>.throwableOrNull: Throwable?
  get() = if (this is ResultOf.Failure) throwable else null

/**
 * The block() will be called when it is a success
 */
inline fun <R> ResultOf<R>.onSuccess(block: (R) -> Unit) {
  if (this is ResultOf.Success) {
    block(value)
  }
}

/**
 * The block() will be called when it is a failure
 */
inline fun <R> ResultOf<R>.onFailure(block: (Throwable?) -> Unit) {
  if (this is ResultOf.Failure) {
    block(throwable)
  }
}

/**
 * Map successful value into something else.
 * @return a new transformed ResultOf.
 */
inline fun <T, R> ResultOf<T>.map(transform: (T) -> R): ResultOf<R> {
  return when (this) {
    is ResultOf.Failure -> this
    is ResultOf.Success -> ResultOf.Success(transform(value))
  }
}

/**
 * Map ResultOf successful value into something else.
 * @return a new transformed ResultOf.
 */
inline fun <T, R> ResultOf<T>.flatMap(transform: (T) -> ResultOf<R>): ResultOf<R> {
  return when (this) {
    is ResultOf.Failure -> this
    is ResultOf.Success -> transform(value)
  }
}

/**
 * Map an throwable into another throwable
 * @return a new transform ResultOf
 */
inline fun <T, R : Throwable> ResultOf<T>.failureMap(transform: (Throwable?) -> R): ResultOf<T> {
  return when (this) {
    is ResultOf.Failure -> ResultOf.Failure(transform(throwable))
    is ResultOf.Success -> this
  }
}

/**
 * Map ResultOf failure's throwable into another throwable
 * @return a new transform ResultOf
 */
inline fun <T> ResultOf<T>.failureFlatMap(transform: (Throwable?) -> ResultOf.Failure): ResultOf<T> {
  return when (this) {
    is ResultOf.Failure -> transform(throwable)
    is ResultOf.Success -> this
  }
}

/**
 * Get value or default value when failed.
 * @return value or default value.
 * @param defaultValue a default to be return when failed.
 */
inline fun <R> ResultOf<R>.valueOrDefault(defaultValue: (Throwable?) -> R): R {
  return when (this) {
    is ResultOf.Failure -> defaultValue(throwable)
    is ResultOf.Success -> value
  }
}

/**
 * @return an instance of ResultOf
 */
inline fun <R> resultOf(block: () -> R): ResultOf<R> {
  return try {
    ResultOf.Success(block())
  } catch (throwable: Throwable) {
    ResultOf.Failure(throwable)
  }
}

/**
 * Convert a nullable ResultOf into a non-null ResultOf.
 */
@Suppress("UNCHECKED_CAST")
inline fun <R> ResultOf<R?>.failureOnNull(): ResultOf<R> {
  return when (this) {
    is ResultOf.Failure -> this
    is ResultOf.Success -> {
      if (value == null) ResultOf.Failure(NullPointerException())
      else this as ResultOf<R>
    }
  }
}

/**
 * Create a ResultOf<T> from a standard Result<T>.
 * @return ResultOf<T>
 */
inline fun <T> Result<T>.asResultOf(): ResultOf<T> = resultOf { getOrThrow() }

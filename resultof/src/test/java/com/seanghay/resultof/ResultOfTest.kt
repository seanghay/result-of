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
package com.seanghay.resultof

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultOfTest {

  @Test
  fun `should be Success`() {
    val result = resultOf { 100 }
    assertTrue(result.isSuccessful)
    assertFalse(result.isFailure)
    assertEquals(result.valueOrNull, 100)
  }

  @Test
  fun `should be Failure`() {
    val result = resultOf { throw IllegalStateException("Oops!") }
    assertFalse(result.isSuccessful)
    assertTrue(result.isFailure)
  }

  @Test(expected = IllegalStateException::class)
  fun `should be throw on Failure`() {
    val result = resultOf { throw IllegalStateException("Oops!") }
    val impossible = result.valueOrThrow
  }

  @Test
  fun `should return value when use valueOrThrow`() {
    val result = resultOf { "hello" }
    assertEquals("hello", result.valueOrThrow)
  }

  @Test
  fun `should return new value when use map`() {
    val result = resultOf { "hello" }.map { it.toUpperCase() }
    assertEquals("HELLO", result.valueOrThrow)
  }

  @Test(expected = RuntimeException::class)
  fun `should return new throwable when use failureMap`() {
    val result = resultOf { throw IllegalArgumentException("args") }.failureMap { RuntimeException() }
    result.valueOrThrow
  }

  @Test
  fun `should return throwable value`() {
    val throwable = IllegalArgumentException("args")
    val result = resultOf { throw throwable }
    assertNotNull(result.throwableOrNull)
    assertEquals(throwable, result.throwableOrNull)
  }

  @Test
  fun `should return default value`() {
    val result = resultOf { throw RuntimeException("oh no") }.valueOrDefault {
      if (it is RuntimeException) "Yes"
      else "No"
    }
    assertEquals("Yes", result)
  }

  @Test
  fun `should return default value 2`() {
    val result = resultOf { throw RuntimeException("oh no") }.valueOrDefault {
      if (it is IllegalArgumentException) "Yes"
      else "No"
    }
    assertEquals("No", result)
  }

  @Test
  fun `flatMap on ResultOf`() {
    val result = resultOf {
      100
    }.flatMap { resultOf { it * 2 } }
    assertEquals(200, result.valueOrThrow)
  }

  @Test(expected = IllegalStateException::class)
  fun `flatMap on ResultOf Failure`() {
    val result = resultOf {
      throw RuntimeException()
    }.failureFlatMap {
      ResultOf.Failure(IllegalStateException("ok"))
    }
    throw result.throwableOrNull!!
  }

  @Test
  fun `convert from stdlib Result to ResultOf`() {
    val result = runCatching {
      100
    }.asResultOf()
    assertEquals(100, result.valueOrThrow)
  }

  @Test(expected = RuntimeException::class)
  fun `convert from stdlib Result to ResultOf and throw`() {
    val result = runCatching {
      throw RuntimeException()
    }.asResultOf()
    result.valueOrThrow
  }

  @Test(expected = NullPointerException::class)
  fun `failure on null`() {
    val result = resultOf { null as String? }.failureOnNull()
    assertFalse(result.isSuccessful)
    assertTrue(result.isFailure)
    result.valueOrThrow
  }
}

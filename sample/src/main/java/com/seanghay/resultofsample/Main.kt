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
@file:JvmName("Main")

package com.seanghay.resultofsample

import com.seanghay.resultof.failureOnNull
import com.seanghay.resultof.flatMap
import com.seanghay.resultof.map
import com.seanghay.resultof.resultOf
import com.seanghay.resultof.valueOrDefault
import com.seanghay.resultof.valueOrNull
import com.seanghay.resultof.valueOrThrow

fun main() {

  val result = resultOf { "Hello, world" }.map { it.toUpperCase() }
  println(result)

  val result2 = resultOf { null }.failureOnNull().valueOrDefault { "Default" }
  println(result2)

  val result3 = resultOf { throw IllegalStateException() }.failureOnNull().valueOrDefault { "Default" }
  println(result3)

  val result4 = resultOf { "Hello, world" }.flatMap { result }

  println(result4.valueOrNull)
  println(result4.valueOrThrow)
  println(result4.valueOrDefault { "Oops!" })
}

package org.aossie.agoraandroid.common.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {

  return lazy {
    GlobalScope.async {
      block.invoke(this)
    }
  }
}

package org.aossie.agoraandroid.ui.di.utils

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.reflect.KClass

@Target(
  FUNCTION,
  PROPERTY_GETTER,
  PROPERTY_SETTER
)
@kotlin.annotation.Retention(RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

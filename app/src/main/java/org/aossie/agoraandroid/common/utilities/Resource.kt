@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package org.aossie.agoraandroid.common.utilities
import android.app.Application
import android.content.Context
import androidx.annotation.StringRes

fun Context.getString(@StringRes resId: Int) = getString(resId)

fun Application.getString(@StringRes resId: Int) = getString(resId)

fun Application.get(@StringRes resId: Int) = getString(resId)

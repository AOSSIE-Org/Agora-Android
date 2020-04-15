package org.aossie.agoraandroid.utilities

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.getString(@StringRes resId: Int) = getString(resId)

//for displaying toast messages for short duration
fun Context.toastShort(toastMessage: String) = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()

//for displaying toast messages for long duration
fun Context.toastLong(toastMessage: String) = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

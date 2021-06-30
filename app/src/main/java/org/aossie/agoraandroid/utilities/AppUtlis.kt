package org.aossie.agoraandroid.utilities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

fun String.isUrl() =
  Pattern.compile(AppConstants.URL_REGEX).matcher(this).matches()

fun Context.browse(url: String) {
  try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
  } catch (e: ActivityNotFoundException) {
    throw e
  }
}

fun Bitmap.toByteArray(type: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray {
  val bos = ByteArrayOutputStream()
  this.compress(type, 10, bos)
  return bos.toByteArray()
}

package org.aossie.agoraandroid.utilities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
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

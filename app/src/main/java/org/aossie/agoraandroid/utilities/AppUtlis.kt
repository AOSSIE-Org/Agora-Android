package org.aossie.agoraandroid.utilities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import org.aossie.agoraandroid.R
import java.util.regex.Pattern

fun String.isUrl() =
  Pattern.compile(AppConstants.URL_REGEX).matcher(this).matches()

fun Context.browse(url: String) {
  try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
  } catch (e: ActivityNotFoundException) {
    Toast.makeText(this, this.resources.getString(R.string.no_browser), Toast.LENGTH_SHORT).show()
  }
}

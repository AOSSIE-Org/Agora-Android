package org.aossie.agoraandroid.utilities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
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

val Context.dataStore by preferencesDataStore("myPrefs")

fun Bitmap.toByteArray(type: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray {
  val bos = ByteArrayOutputStream()
  this.compress(type, 10, bos)
  return bos.toByteArray()
}

fun subscribeToFCM(mail: String?) {
  mail?.let {
    if (it.contains("@")) {
      it.substring(0, it.indexOf("@")).let { topic ->
        Firebase.messaging.subscribeToTopic(topic)
      }
    }
  }
}

fun unsubscribeFromFCM(mail: String?) {
  mail?.let {
    if (it.contains("@")) {
      it.substring(0, it.indexOf("@")).let { topic ->
        Firebase.messaging.unsubscribeFromTopic(topic)
      }
    }
  }
}

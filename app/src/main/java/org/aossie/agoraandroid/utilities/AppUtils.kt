package org.aossie.agoraandroid.utilities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.animation.DecelerateInterpolator
import androidx.datastore.preferences.preferencesDataStore
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.Spotlight
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.utilities.AppConstants.SPOTLIGHT_ANIMATION_DURATION
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

fun String.isUrl() =
  Pattern.compile(AppConstants.URL_REGEX)
    .matcher(this)
    .matches()

fun Context.browse(url: String) {
  try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
  } catch (e: ActivityNotFoundException) {
    throw e
  }
}

val Context.userDataStore by preferencesDataStore("userPrefs")
val Context.spotlightDataStore by preferencesDataStore("spotlightPrefs")

fun Bitmap.toByteArray(type: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray {
  val bos = ByteArrayOutputStream()
  this.compress(type, 10, bos)
  return bos.toByteArray()
}

fun Activity.getSpotlight(
  targetData: TargetData,
  dismissSpotlight: () -> Unit,
  skipSpotlight: () -> Unit,
  showNextSpotlight: () -> Unit
): Spotlight {
  return Spotlight.Builder(this)
    .setTargets(getTarget(this, targetData, dismissSpotlight, skipSpotlight))
    .setBackgroundColorRes(color.spotlight_background)
    .setDuration(SPOTLIGHT_ANIMATION_DURATION)
    .setAnimation(DecelerateInterpolator(2f))
    .setOnSpotlightListener(object : OnSpotlightListener {
      override fun onEnded() {
        showNextSpotlight.invoke()
      }

      override fun onStarted() {
      }
    })
    .build()
}

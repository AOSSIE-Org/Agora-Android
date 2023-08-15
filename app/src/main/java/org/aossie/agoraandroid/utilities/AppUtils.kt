package org.aossie.agoraandroid.utilities

import android.app.KeyguardManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
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

fun Context.canAuthenticateBiometric(): Boolean {
  val biometricManager = BiometricManager.from(this)

  val canAuthenticate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
    biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL or BiometricManager.Authenticators.BIOMETRIC_STRONG or BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
  else
    biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
  return canAuthenticate && isSecured()
}

private fun Context.isSecured(): Boolean {
  val keyguardManager = getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    keyguardManager.isDeviceSecure
  else keyguardManager.isKeyguardSecure
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

fun NavController.navigateSafely(direction: NavDirections) {
  val currentDestination = currentDestination
  if (currentDestination != null) {
    val navAction = currentDestination.getAction(direction.actionId)
    if (navAction != null) {
      val destinationId: Int = navAction.destinationId ?: 0
      val currentNode: NavGraph? =
        if (currentDestination is NavGraph) currentDestination else currentDestination.parent
      if (destinationId != 0 && currentNode != null && currentNode.findNode(destinationId) != null) {
        navigate(direction)
      }
    }
  }
}
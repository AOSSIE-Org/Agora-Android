package org.aossie.agoraandroid.utilities

import android.R.string
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.aossie.agoraandroid.R
import java.io.File

fun ProgressBar.show() {
  visibility = ProgressBar.VISIBLE
}

fun ProgressBar.hide() {
  visibility = ProgressBar.GONE
}

fun TextView.hide() {
  visibility = TextView.GONE
}

fun TextView.show() {
  visibility = TextView.VISIBLE
}

fun View.snackbar(message: String?) {
  val msg: String = message ?: context.getString(R.string.something_went_wrong_please_try_again_later)
  if (msg.isNotEmpty()) {
    Snackbar.make(this, msg, Snackbar.LENGTH_INDEFINITE)
      .also { snackbar ->
        snackbar.setAction(AppConstants.ok) {
          snackbar.dismiss()
        }
          .show()
      }
  }
}

fun View.errorDialog(message: String) {
  AlertDialog.Builder(context)
    .setTitle("Alert ! ! !")
    .setMessage(message)
    .setCancelable(false)
    .setPositiveButton(string.ok) { dialog, _ ->
      dialog.cancel()
    }
    .create()
    .show()
}

fun View.shortSnackbar(message: String?) {
  val msg: String = message ?: context.getString(R.string.something_went_wrong_please_try_again_later)
  if (msg.isNotEmpty()) {
    Snackbar
      .make(this, msg, Snackbar.LENGTH_SHORT)
      .show()
  }
}

fun BottomNavigationView.animVisible() {
  if (visibility == View.GONE) {
    visibility = View.VISIBLE
    animation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
  }
}

fun BottomNavigationView.animGone() {
  if (visibility == View.VISIBLE) {
    visibility = View.GONE
    animation = AnimationUtils.loadAnimation(context, R.anim.slide_down)
  }
}

fun View.toggleIsEnable() {
  isEnabled = !isEnabled
}

fun View.enableView() {
  isEnabled = true
}

fun View.disableView() {
  isEnabled = false
}

fun ImageView.loadImage(url: String, networkPolicy: NetworkPolicy? = null, onError: () -> Unit = {}) {
  Picasso.get().load(url).apply {
    if (networkPolicy != null) networkPolicy(networkPolicy)
    placeholder(ContextCompat.getDrawable(this@loadImage.context, R.drawable.ic_user)!!)
      .into(
        this@loadImage,
        object : Callback {
          override fun onSuccess() {
            // Do Nothing
          }
          override fun onError(e: Exception?) {
            onError.invoke()
          }
        }
      )
  }
}

fun ImageView.loadImageFromMemoryNoCache(file: File) {
  Picasso.get().load(file)
    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
    .placeholder(ContextCompat.getDrawable(this.context, R.drawable.ic_user)!!)
    .into(this)
}

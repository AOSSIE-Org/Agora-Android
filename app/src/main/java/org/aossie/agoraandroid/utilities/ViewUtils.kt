package org.aossie.agoraandroid.utilities

import android.R.string
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.aossie.agoraandroid.R

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

fun View.snackbar(message: String) {
  Snackbar
      .make(this, message, Snackbar.LENGTH_INDEFINITE)
      .also { snackbar ->
        snackbar.setAction(AppConstants.ok) {
          snackbar.dismiss()
        }
            .show()
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

fun View.shortSnackbar(message: String) {
  Snackbar
      .make(this, message, Snackbar.LENGTH_SHORT)
      .show()
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
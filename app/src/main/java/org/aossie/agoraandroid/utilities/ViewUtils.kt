package org.aossie.agoraandroid.utilities

import android.R.string
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CalendarView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.aossie.agoraandroid.R

fun Fragment.hideActionBar() = (activity as AppCompatActivity).supportActionBar!!.hide()
fun Fragment.showActionBar() = (activity as AppCompatActivity).supportActionBar!!.show()

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
        snackbar.setAction(AppConstants.ok, View.OnClickListener {
          snackbar.dismiss()
        })
            .show()
      }
}

fun View.errorDialog(message: String) {
  AlertDialog.Builder(context)
      .setTitle("Alert ! ! !")
      .setMessage(message)
      .setCancelable(false)
      .setPositiveButton(string.ok) { dialog, which ->
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

fun CalendarView.animVisible() {
  visibility = View.VISIBLE
  animation = AnimationUtils.loadAnimation(context, R.anim.slide_down)
}

fun CalendarView.animGone() {
  animation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
  visibility = View.GONE
}